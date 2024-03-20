package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
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
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private InvPreviewExtService invPreviewExtService;
    @Autowired
    private PaySteamOrderService paySteamOrderService;
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
            throw new ServiceException(-1, "饰品不能上架,请检查是否冷却中或不能交易");
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
            if (itemPriceInfo.getPrice() == null || itemPriceInfo.getPrice() <= 0) {
                throw new ServiceException(-1, "未设置价格");
            }
            sellingDO.setPrice(itemPriceInfo.getPrice());
            sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            sellingMapper.insert(sellingDO);
            invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
        }
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
        }
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


    public PageResult<SellingMergeListReqVo> sellingMerge(SellingPageReqVO sellingPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (sellingPageReqVO.getSteamId() != null && !sellingPageReqVO.getSteamId().equals("")) {
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getUserType, loginUser.getUserType())
                    .eq(SellingDO::getUserId, loginUser.getId())
                    .eq(SellingDO::getSteamId, sellingPageReqVO.getSteamId())
                    .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));


            if (Objects.isNull(sellingDOS) || sellingDOS.size() <= 0) {
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
                    } else {
                        sellingPageReqVO1.setItemInfo(new C5ItemInfo());
                        sellingPageReqVO1.setMinPrice(0);
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
        } else {
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getUserType, loginUser.getUserType())
                    .eq(SellingDO::getUserId, loginUser.getId())
                    .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));

            if (Objects.isNull(sellingDOS) || sellingDOS.size() <= 0) {
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
                    } else {
                        sellingPageReqVO1.setItemInfo(new C5ItemInfo());
                        sellingPageReqVO1.setMinPrice(0);
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
}

