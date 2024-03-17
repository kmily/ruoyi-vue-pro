package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvMergeToSellPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 在售饰品 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
@Slf4j
public class SellingExtService {

    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private SellingService sellingService;
    @Resource
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private InvPreviewExtService invPreviewExtService;
    @Autowired
    private PaySteamOrderService paySteamOrderService;
    @Resource
    private InvDescService invDescService;
    @Resource
    private InvPreviewMapper invPreviewMapper;

    @Transactional(rollbackFor = ServiceException.class)
    public void batchSale(BatchSellReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchSellReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }
        List<InvDO> invDOList = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .inIfPresent(InvDO::getId, invIds)
                .eq(InvDO::getUserId, loginUser.getId())
                .eq(InvDO::getUserType, loginUser.getUserType())
        );
        if (invIds.size() != invDOList.size()) {
            throw new ServiceException(-1, "检测到你上线的库存不是所有库存均为可上线状态，请检查后再提交");
        }
        List<Long> invDescId = invDOList.stream().map(InvDO::getInvDescId).collect(Collectors.toList());
        List<InvDescDO> invDescDO2 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .in(InvDescDO::getId, invDescId));
        long count = invDescDO2.stream().filter(item -> item.getTradable() == 0).count();
        if (count > 0) {
            throw new ServiceException(-1, "饰品不能上架,请检查是否冷却中或其他");
        }
        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds)
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
        if (!sellingDOInSelling.isEmpty()) {
            throw new ServiceException(-1, "部分商品已上架构，请检查后再操作上架构");
        }
        for (InvDO item : invDOList) {
            Optional<BatchSellReqVo.Item> first = reqVo.getItems().stream().filter(i -> i.getId().equals(item.getId())).findFirst();
            item.setPrice(invDOList.get(0).getPrice());
            if (!first.isPresent()) {
                log.error("价格信息未查找到{},{}", item, reqVo.getItems());
                throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
            }
            BatchSellReqVo.Item itemPriceInfo = first.get();
            item.setPrice(itemPriceInfo.getPrice());
//            InvDO invDO = invDOS.get(0);
            // 商品上架流程
//            invDO.setPrice(invPageReqVos.getPrice());
            item.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            invMapper.updateById(item);
            Optional<InvDescDO> invDescDO = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getClassid, item.getClassid())
                    .eq(InvDescDO::getInstanceid, item.getInstanceid())).stream().findFirst();
            if (!invDescDO.isPresent()) {
                throw new ServiceException(-1, "exists");
            }
            Long l = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getId, item.getId())
            );
            if (l > 0) {
                throw new ServiceException(-1, "exists");
            }
            SellingDO sellingDO = new SellingDO();
            sellingDO.setAppid(item.getAppid());
            sellingDO.setAssetid(item.getAssetid());
            sellingDO.setClassid(item.getClassid());
            sellingDO.setInstanceid(item.getInstanceid());
            sellingDO.setAmount(item.getAmount());
            sellingDO.setSteamId(item.getSteamId());
            sellingDO.setUserId(item.getUserId());
            sellingDO.setUserType(item.getUserType());
            sellingDO.setBindUserId(item.getBindUserId());
            sellingDO.setContextid(item.getContextid());
            sellingDO.setInvDescId(invDescDO.get().getId());
            sellingDO.setSelExterior(invDescDO.get().getSelExterior());
            sellingDO.setSelItemset(invDescDO.get().getSelItemset());
            sellingDO.setSelQuality(invDescDO.get().getSelQuality());
            sellingDO.setSelRarity(invDescDO.get().getSelRarity());
            sellingDO.setSelType(invDescDO.get().getSelType());
            sellingDO.setSelWeapon(invDescDO.get().getSelWeapon());
            sellingDO.setMarketName(invDescDO.get().getMarketName());
            sellingDO.setIconUrl(invDescDO.get().getIconUrl());
            sellingDO.setMarketHashName(invDescDO.get().getMarketHashName());
            sellingDO.setInvId(item.getId());
            if (itemPriceInfo.getPrice() == null || itemPriceInfo.getPrice() == 0) {
                throw new ServiceException(-1, "未设置价格");
            }
            sellingDO.setPrice(itemPriceInfo.getPrice());
            sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            sellingMapper.insert(sellingDO);
            invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
        }
    }

    /**
     * @param invPageReqVos
     * @return
     */
    public SellingDO getToSale(InvPageReqVo invPageReqVos) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        List<InvDO> invDO2 = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getId, invPageReqVos.getId()));

        List<InvDescDO> invDescDO2 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .eq(InvDescDO::getId, invDO2.get(0).getInvDescId()));

        if (invDescDO2.get(0).getTradable() == 0) {
            throw new ServiceException(-1, "饰品不能上架,请检查是否冷却中或其他");
        }

        // 判断用户库存是否能上架
/*
        if (invDescMapper.selectList().get(0).getTradable() == 0) {
            throw new ServiceException(-1, "饰品不能上架,请检查");
        }
*/

        // 判断用户是否上架指定为自己的库存
        List<InvDO> invDOS = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getId, invPageReqVos.getId())
                .eqIfPresent(InvDO::getUserType, loginUser.getUserType())
                .eq(InvDO::getUserId, loginUser.getId()));
        if (invDOS.isEmpty()) {
            throw new ServiceException(-1, "操作异常，没有权限");
        }

        // 获取用户和在售列表匹配
        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .eq(SellingDO::getInvId, invDOS.get(0).getId())
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
        if (!sellingDOS.isEmpty()) {
            throw new ServiceException(-1, "商品已上架");
        }

        InvDO invDO = invDOS.get(0);
        // 商品上架流程
        invDO.setPrice(invPageReqVos.getPrice());
        invDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
        invMapper.updateById(invDO);
        Optional<InvDescDO> invDescDO = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .eq(InvDescDO::getClassid, invDO.getClassid())
                .eq(InvDescDO::getInstanceid, invDO.getInstanceid())).stream().findFirst();
        if (!invDescDO.isPresent()) {
            throw new ServiceException(-1, "exists");
        }
        Long l = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getId, invPageReqVos.getId())
        );
        if (l > 0) {
            throw new ServiceException(-1, "exists");
        }
        SellingDO sellingDO = new SellingDO();
        sellingDO.setAppid(invDO.getAppid());
        sellingDO.setAssetid(invDO.getAssetid());
        sellingDO.setClassid(invDO.getClassid());
        sellingDO.setInstanceid(invDO.getInstanceid());
        sellingDO.setAmount(invDO.getAmount());
        sellingDO.setSteamId(invDO.getSteamId());
        sellingDO.setUserId(invDO.getUserId());
        sellingDO.setUserType(invDO.getUserType());
        sellingDO.setBindUserId(invDO.getBindUserId());
        sellingDO.setContextid(invDO.getContextid());
        sellingDO.setInvDescId(invDescDO.get().getId());
        sellingDO.setSelExterior(invDescDO.get().getSelExterior());
        sellingDO.setSelItemset(invDescDO.get().getSelItemset());
        sellingDO.setSelQuality(invDescDO.get().getSelQuality());
        sellingDO.setSelRarity(invDescDO.get().getSelRarity());
        sellingDO.setSelType(invDescDO.get().getSelType());
        sellingDO.setSelWeapon(invDescDO.get().getSelWeapon());
        sellingDO.setMarketName(invDescDO.get().getMarketName());
        sellingDO.setIconUrl(invDescDO.get().getIconUrl());
        sellingDO.setMarketHashName(invDescDO.get().getMarketHashName());
        sellingDO.setInvId(invDO.getId());
        if (invPageReqVos.getPrice() == null || invPageReqVos.getPrice() == 0) {
            throw new ServiceException(-1, "未设置价格");
        }

        sellingDO.setPrice(invPageReqVos.getPrice());
        sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
        sellingMapper.insert(sellingDO);

        invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());

        return sellingDO;
    }

    @Transactional(rollbackFor = ServiceException.class)
    public void batchOffSale(BatchOffSellReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchOffSellReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }
        List<InvDO> invDOList = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .inIfPresent(InvDO::getId, invIds)
                .eq(InvDO::getUserId, loginUser.getId())
                .eq(InvDO::getUserType, loginUser.getUserType())
        );
        if (invIds.size() != invDOList.size()) {
            throw new ServiceException(-1, "检测到你下线的库存部分不存在，请检查后再提交");
        }
        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds));

        long count1 = sellingDOInSelling.stream().filter(item -> Arrays.asList(
                InvTransferStatusEnum.INORDER.getStatus(),
                InvTransferStatusEnum.TransferFINISH.getStatus(),
                InvTransferStatusEnum.OFF_SALE.getStatus(),
                InvTransferStatusEnum.TransferERROR.getStatus()).contains(item.getTransferStatus())).count();

        if (count1 > 0) {
            throw new ServiceException(-1, "部分商品已不存在，请检查后再操作下架");
        }
        for (InvDO item : invDOList) {
            item.setPrice(null);
            item.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
            invMapper.updateById(item);
        }
        for (SellingDO item : sellingDOInSelling) {
            if (paySteamOrderService.getExpOrder(item.getId()).size() > 0) {
                throw new ServiceException(-1, "商品交易中，不允许下架");
            }
            sellingMapper.deleteById(item.getId());
            invPreviewExtService.markInvEnable(item.getMarketHashName());
        }
    }

    public Optional<SellingDO> getOffSale(@Valid SellingReqVo sellingReqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        // 访问用户库存
        InvDO invDO = invMapper.selectById(sellingReqVo.getId());
        // 获取当前上架信息
        Optional<SellingDO> sellingDO1 = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getInvId, invDO.getId())
                .eq(SellingDO::getClassid, invDO.getClassid())
                .in(SellingDO::getTransferStatus, Arrays.asList(InvTransferStatusEnum.SELL.getStatus(),
                        InvTransferStatusEnum.INORDER.getStatus(),
                        InvTransferStatusEnum.TransferFINISH.getStatus(),
                        InvTransferStatusEnum.OFF_SALE.getStatus(),
                        InvTransferStatusEnum.TransferERROR.getStatus()))
                .eq(SellingDO::getInstanceid, invDO.getInstanceid())).stream().findFirst();


        if (paySteamOrderService.getExpOrder(sellingDO1.get().getId()).size() > 0) {
            throw new ServiceException(-1, "商品交易中，不允许下架");
        }

        // 在售账号是否是自己账号
        if (!sellingDO1.get().getUserId().equals(loginUser.getId())) {
            throw new ServiceException(-1, "无权限");
        }
        if (!sellingDO1.get().getUserType().equals(loginUser.getUserType())) {
            throw new ServiceException(-1, "无权限");
        }
        log.info(String.valueOf(sellingDO1.get().getId()));
        Long id = sellingDO1.get().getId();
        // 下架(设置transferstatus为‘0’未出售)
        invDO.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
        invDO.setPrice(null);
        invMapper.updateById(invDO);
        sellingMapper.deleteById(id);

        invPreviewExtService.markInvEnable(sellingDO1.get().getMarketHashName());

        return sellingDO1;
    }

    /**
     * 批量修改价格
     *
     * @param reqVo
     */

    @Transactional(rollbackFor = ServiceException.class)
    public void batchChangePrice(BatchChangePriceReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchChangePriceReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }

        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds)
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));

        if (Objects.isNull(sellingDOInSelling)) {
            throw new ServiceException(-1, "商品不存在或已下架");
        }

        if (sellingDOInSelling.size() != invIds.size()) {
            throw new ServiceException(-1, "部分商品已不存在，请检查后再操作改价");
        }

        for (SellingDO item : sellingDOInSelling) {
            Optional<BatchChangePriceReqVo.Item> first = reqVo.getItems().stream().filter(i -> i.getId().equals(item.getInvId())).findFirst();
            if (first.isPresent()) {
                BatchChangePriceReqVo.Item item1 = first.get();
                item.setPrice(item1.getPrice());
                sellingMapper.updateById(item);
                invPreviewExtService.markInvEnable(item.getMarketHashName());
            } else {
                throw new ServiceException(-1, "信息不正确，请检查");
            }
        }
    }

    /**
     * 修改价格
     *
     * @param reqVo
     * @return
     */
    public Integer changePrice(SellingChangePriceReqVo reqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        // 访问用户库存
        InvDO invDO = invMapper.selectById(reqVo.getId());
        // 获取当前上架信息
        SellingDO sellingDO = sellingMapper.selectById(reqVo.getId());
        if (Objects.isNull(sellingDO)) {
            throw new ServiceException(-1, "上架商品不存在");
        }
        if (Objects.isNull(reqVo.getPrice()) || reqVo.getPrice() < 0L) {
            throw new ServiceException(-1, "商品价格不合法");
        }
        if (!sellingDO.getUserId().equals(loginUser.getId())) {
            throw new ServiceException(-1, "无权限");
        }
        if (!sellingDO.getUserType().equals(loginUser.getUserType())) {
            throw new ServiceException(-1, "无权限");
        }
        // 用户主动下架,当前饰品还存在用户steam库存中
        if (CommonStatusEnum.isDisable(sellingDO.getStatus())) {
            // 是否在库存
            throw new ServiceException(-1, "商品未上架");
        }
        if (sellingDO.getTransferStatus() == InvTransferStatusEnum.INORDER.getStatus()) {
            throw new ServiceException(-1, "商品已下单");
        }
        int i = sellingMapper.updateById(new SellingDO().setId(reqVo.getId()).setPrice(reqVo.getPrice()));
        return i;
    }


    //  出售未合并
    public PageResult<SellingRespVO> sellingUnMerge(SellingPageReqVO sellingPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        PageResult<SellingDO> sellingPage = sellingService.getSellingPage(sellingPageReqVO);

        List<SellingDO> sellingDO1 = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId()));

        List<SellingRespVO> sellingRespVOList = new ArrayList<>();
        for (SellingDO sellingDO : sellingDO1) {
            SellingRespVO sellingDOS = new SellingRespVO();
            sellingDOS.setAppid(sellingDO.getAppid());
            sellingDOS.setAssetid(sellingDO.getAssetid());
            sellingDOS.setClassid(sellingDO.getClassid());
            sellingDOS.setInstanceid(sellingDO.getInstanceid());
            sellingDOS.setAmount(sellingDO.getAmount());
            sellingDOS.setSteamId(sellingDO.getSteamId());
            sellingDOS.setUserId(sellingDO.getUserId());
            sellingDOS.setUserType(sellingDO.getUserType());
            sellingDOS.setBindUserId(sellingDO.getBindUserId());
            sellingDOS.setContextid(sellingDO.getContextid());
            sellingDOS.setInvDescId(sellingDO.getInvDescId());
            sellingDOS.setSelExterior(sellingDO.getSelExterior());
            sellingDOS.setSelItemset(sellingDO.getSelItemset());
            sellingDOS.setSelQuality(sellingDO.getSelQuality());
            sellingDOS.setSelRarity(sellingDO.getSelRarity());
            sellingDOS.setSelType(sellingDO.getSelType());
            sellingDOS.setSelWeapon(sellingDO.getSelWeapon());
            sellingDOS.setMarketName(sellingDO.getMarketName());
            sellingDOS.setMarketHashName(sellingDO.getMarketHashName());
            sellingDOS.setIconUrl(sellingDO.getIconUrl());
            sellingDOS.setInvId(sellingDO.getInvId());
            sellingDOS.setTransferStatus(sellingDO.getTransferStatus());
            sellingDOS.setStatus(sellingDO.getStatus());
            sellingDOS.setPrice(sellingDO.getPrice());
            sellingRespVOList.add(sellingDOS);
        }

        return new PageResult<>(sellingRespVOList, sellingPage.getTotal());
    }

    public PageResult<SellingMergeListReqVo> sellingMerge(SellingPageReqVO sellingPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));
        if (Objects.isNull(sellingDOS) || sellingDOS.size() < 0) {

            return new PageResult<>(Collections.emptyList(), 0L);
        }

        List<String> sellingHashNameList = sellingDOS.stream().map(SellingDO::getMarketHashName).distinct().collect(Collectors.toList());
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .in(InvPreviewDO::getMarketHashName, sellingHashNameList));
        Map<String, InvPreviewDO> mapInvPreview = new HashMap<>();
        if (Objects.nonNull(invPreviewDOS)) {
            mapInvPreview = invPreviewDOS.stream().collect(Collectors.toMap(InvPreviewDO::getMarketHashName, i -> i, (v1, v2) -> v1));
        }


        Map<String, SellingMergeListReqVo> invPage = new HashMap<>();
        for (SellingDO element : sellingDOS) {
            if (Objects.nonNull(invPage.get(element.getMarketName()))) {
                SellingMergeListReqVo sellingMergeListReqVo = invPage.get(element.getMarketName());

                List<Integer> list1 = new ArrayList<>(sellingMergeListReqVo.getPrice());
                list1.add(Integer.valueOf(element.getPrice()));
                sellingMergeListReqVo.setPrice(list1);

                ArrayList<String> list = new ArrayList<>(sellingMergeListReqVo.getInvId());
                list.add(String.valueOf(element.getInvId()));
                sellingMergeListReqVo.setInvId(list);
                sellingMergeListReqVo.setNumber(list.size());

            } else {
                SellingMergeListReqVo sellingPageReqVO1 = new SellingMergeListReqVo();

                InvPreviewDO invPreviewDO = mapInvPreview.get(element.getMarketHashName());

                if (Objects.nonNull(invPreviewDO)) {
                    sellingPageReqVO1.setItemInfo(invPreviewDO.getItemInfo());
                    sellingPageReqVO1.setMinPrice(invPreviewDO.getMinPrice());
                }
                sellingPageReqVO1.setMarketHashName(element.getMarketHashName());
                sellingPageReqVO1.setIconUrl(element.getIconUrl());
                sellingPageReqVO1.setSelType(element.getSelType());
                sellingPageReqVO1.setSelExterior(element.getSelExterior());
                sellingPageReqVO1.setSelItemset(element.getSelItemset());
                sellingPageReqVO1.setSelQuality(element.getSelQuality());
                sellingPageReqVO1.setSelRarity(element.getSelRarity());
                sellingPageReqVO1.setSelWeapon(element.getSelWeapon());
                sellingPageReqVO1.setAssetId(element.getAssetid());
                sellingPageReqVO1.setMarketName(element.getMarketName());
                sellingPageReqVO1.setStatus(element.getStatus());
                sellingPageReqVO1.setTransferStatus(element.getTransferStatus());
                sellingPageReqVO1.setInvId(Arrays.asList(String.valueOf(element.getInvId())));
                sellingPageReqVO1.setPrice(Arrays.asList(element.getPrice()));
                invPage.put(element.getMarketName(), sellingPageReqVO1);
            }
        }

        List<SellingMergeListReqVo> sellingMergePage = new ArrayList<>();
        for (Map.Entry<String, SellingMergeListReqVo> key : invPage.entrySet()) {
            sellingMergePage.add(key.getValue());
        }

        return new PageResult<>(sellingMergePage, (long) sellingDOS.size());
    }
}

