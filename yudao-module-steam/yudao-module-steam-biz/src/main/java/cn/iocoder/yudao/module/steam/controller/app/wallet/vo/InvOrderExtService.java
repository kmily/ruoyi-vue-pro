package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.SellOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
@Slf4j
public class InvOrderExtService {
    @Resource
    private InvOrderMapper invOrderMapper;
    @Resource
    private InvPreviewMapper invPreviewMapper;
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private ApiOrderMapper apiOrderMapper;
    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;



    // 出售记录
    public PageResult<SellingDoList> getSellOrder(SellOrderPageReqVO sellReqVo, Page page, LoginUser loginUser) {

        LambdaQueryWrapper<ApiOrderDO> apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                .eq(ApiOrderDO::getSellUserId, loginUser.getId())
                .eq(ApiOrderDO::getSellUserType, loginUser.getUserType());

        if (sellReqVo.getTransferStatus() != null) {
            apiOrderDO.eq(ApiOrderDO::getTransferStatus, sellReqVo.getTransferStatus());
        } else {
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            apiOrderDO.in(ApiOrderDO::getTransferStatus, statusesToMatch);
        }

        apiOrderDO.orderByDesc(ApiOrderDO::getCreateTime);
        List<ApiOrderDO> apiOrderDOList = apiOrderMapper.selectList(apiOrderDO);

        LambdaQueryWrapper<InvOrderDO> invOrderDO = new LambdaQueryWrapper<InvOrderDO>()
                .eq(InvOrderDO::getSellUserId, loginUser.getId())
                .eq(InvOrderDO::getUserType, loginUser.getUserType());

        if (sellReqVo.getTransferStatus() != null) {
            invOrderDO.eq(InvOrderDO::getTransferStatus, sellReqVo.getTransferStatus());
        } else {
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            invOrderDO.in(InvOrderDO::getTransferStatus, statusesToMatch);
        }

        invOrderDO.orderByDesc(InvOrderDO::getCreateTime);
        List<InvOrderDO> invOrderDOList = invOrderMapper.selectList(invOrderDO);

        List<SellingDoList> combinedList = new ArrayList<>();
        for (ApiOrderDO apiOrderDO1 : apiOrderDOList) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(apiOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(apiOrderDO1.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(apiOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(apiOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(apiOrderDO1.getTransferStatus());

            ApiOrderExtDO apiOrderExtDOS = apiOrderExtMapper.selectOne(new LambdaQueryWrapperX<ApiOrderExtDO>()
                    .eqIfPresent(ApiOrderExtDO::getOrderId, apiOrderDO1.getId()));

            if (apiOrderExtDOS == null) {
                apiOrderExtDOS = new ApiOrderExtDO();
            }

            SellingDO sellingDO = sellingMapper.selectById(Long.valueOf(apiOrderExtDOS.getCommodityInfo()));
            if (sellingDO == null) {
                sellingDO = new SellingDO();
            }

            InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eqIfPresent(InvPreviewDO::getMarketHashName, sellingDO.getMarketHashName()));
            if (invPreviewDOS == null) {
                invPreviewDOS = new InvPreviewDO();
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS;

            sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());

            combinedList.add(sellingDoListTemp);
        }

        for (InvOrderDO invOrderDO1 : invOrderDOList) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(invOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(invOrderDO1.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(invOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(invOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(invOrderDO1.getTransferStatus());

            InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eqIfPresent(InvPreviewDO::getItemName, invOrderDO1.getMarketName()));
            if (invPreviewDOS == null) {
                invPreviewDOS = new InvPreviewDO();
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS;

            sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());

            combinedList.add(sellingDoListTemp);
        }

        combinedList.sort(Comparator.comparing(SellingDoList::getCreateTime).reversed());

        long pageSize = page.getSize();
        long pageNo = page.getCurrent();
        long startIndex = (pageNo - 1) * pageSize;
        long endIndex = Math.min(startIndex + pageSize, combinedList.size());
        List<SellingDoList> paginatedList = combinedList.subList((int) startIndex, (int) endIndex);

        return new PageResult<>(paginatedList, (long) combinedList.size());
    }

    // 购买记录
    public PageResult<SellingDoList> getPurchaseOrder(SellOrderPageReqVO sellReqVo, Page page, LoginUser loginUser) {
        LambdaQueryWrapper<ApiOrderDO> apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                .eq(ApiOrderDO::getBuyUserId, loginUser.getId())
                .eq(ApiOrderDO::getBuyUserType, loginUser.getUserType());

        if (sellReqVo.getTransferStatus() != null) {
            apiOrderDO.eq(ApiOrderDO::getTransferStatus, sellReqVo.getTransferStatus());
        } else {
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            apiOrderDO.in(ApiOrderDO::getTransferStatus, statusesToMatch);
        }

        apiOrderDO.orderByDesc(ApiOrderDO::getCreateTime);
        List<ApiOrderDO> apiOrderDOList = apiOrderMapper.selectList(apiOrderDO);

        LambdaQueryWrapper<InvOrderDO> invOrderDO = new LambdaQueryWrapper<InvOrderDO>()
                .eq(InvOrderDO::getUserId, loginUser.getId())
                .eq(InvOrderDO::getUserType, loginUser.getUserType());

        if (sellReqVo.getTransferStatus() != null) {
            invOrderDO.eq(InvOrderDO::getTransferStatus, sellReqVo.getTransferStatus());
        } else {
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            invOrderDO.in(InvOrderDO::getTransferStatus, statusesToMatch);
        }

        invOrderDO.orderByDesc(InvOrderDO::getCreateTime);
        List<InvOrderDO> invOrderDOList = invOrderMapper.selectList(invOrderDO);

        List<SellingDoList> combinedList = new ArrayList<>();
        for (ApiOrderDO apiOrderDO1 : apiOrderDOList) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(apiOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(apiOrderDO1.getPayAmount());
            sellingDoListTemp.setMerchantNo(apiOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(apiOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(apiOrderDO1.getTransferStatus());

            ApiQueryCommodityReqVo apiQueryCommodityReqVo = apiOrderDO1.getBuyInfo();

            if (apiQueryCommodityReqVo != null) {
                InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                        .eqIfPresent(InvPreviewDO::getMarketHashName, apiQueryCommodityReqVo.getCommodityHashName()));

                if (invPreviewDOS == null) {
                    invPreviewDOS = new InvPreviewDO();
                }

                sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
                sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
                sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
                sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

                InvPreviewDO matchingInvPreviewDO = invPreviewDOS;
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            }

            combinedList.add(sellingDoListTemp);
        }

        for (InvOrderDO invOrderDO1 : invOrderDOList) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(invOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(invOrderDO1.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(invOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(invOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(invOrderDO1.getTransferStatus());

            InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eqIfPresent(InvPreviewDO::getItemName, invOrderDO1.getMarketName()));
            if (invPreviewDOS == null) {
                invPreviewDOS = new InvPreviewDO();
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS;
            sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());

            combinedList.add(sellingDoListTemp);
        }

        combinedList.sort(Comparator.comparing(SellingDoList::getCreateTime).reversed());

        long pageSize = page.getSize();
        long pageNo = page.getCurrent();
        long startIndex = (pageNo - 1) * pageSize;
        long endIndex = Math.min(startIndex + pageSize, combinedList.size());
        List<SellingDoList> paginatedList = combinedList.subList((int) startIndex, (int) endIndex);

        return new PageResult<>(paginatedList, (long) combinedList.size());
    }

    // 出售详情
    public PageResult<SellingDoList> getSellOrderDetail(SellOrderPageReqVO reqVo, Page<ApiOrderDO> page, LoginUser loginUser) {
        // 下单状态
        List<SellingDoList> sellingDoLists = new ArrayList<>();
        LambdaQueryWrapper<ApiOrderDO> apiOrderDO;
        // 匹配订单状态
        if(reqVo.getTransferStatus() != null){
            apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                    .eq(ApiOrderDO::getSellUserId, loginUser.getId())
                    .eq(ApiOrderDO::getSellUserType, loginUser.getUserType())
                    .eq(ApiOrderDO::getTransferStatus, reqVo.getTransferStatus())
                    .orderByDesc(ApiOrderDO::getCreateTime);
        }else{
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                    .eq(ApiOrderDO::getSellUserId, loginUser.getId())
                    .eq(ApiOrderDO::getSellUserType, loginUser.getUserType())
                    .in(ApiOrderDO::getTransferStatus, statusesToMatch)
                    .orderByDesc(ApiOrderDO::getCreateTime);
        }

        // 执行分页查询
        IPage<ApiOrderDO> apiOrderDOIPage = apiOrderMapper.selectPage(page, apiOrderDO);

        // 判断
        if (apiOrderDO.isEmptyOfWhere()) {
            return new PageResult<>(sellingDoLists, 0L);
        }
        // 遍历订单，返回订单号等关键数据
        for (ApiOrderDO apiOrderDO1 : apiOrderDOIPage.getRecords()) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(apiOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(apiOrderDO1.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(apiOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(apiOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(apiOrderDO1.getTransferStatus());

            ApiOrderExtDO apiOrderExtDOS = apiOrderExtMapper.selectOne(new LambdaQueryWrapperX<ApiOrderExtDO>()
                    .eqIfPresent(ApiOrderExtDO::getOrderId, apiOrderDO1.getId()));

            if(apiOrderExtDOS == null){
                apiOrderExtDOS = new ApiOrderExtDO();
            }

            // 查找selling表返回相应字段
            SellingDO apiOrderExtDOList = sellingMapper.selectById(Long.valueOf(apiOrderExtDOS.getCommodityInfo()));
            if(apiOrderExtDOList == null){
                apiOrderExtDOList = new SellingDO();
            }

            InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eqIfPresent(InvPreviewDO::getMarketHashName, apiOrderExtDOList.getMarketHashName()));
            if(invPreviewDOS== null){
                invPreviewDOS = new InvPreviewDO();
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS;

            if (matchingInvPreviewDO != null) {
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            } else {
                sellingDoListTemp.setSelExteriorColor((invPreviewDOS.getItemInfo()).getExteriorColor());
            }
            sellingDoLists.add(sellingDoListTemp);
        }
        return new PageResult<>(sellingDoLists, apiOrderDOIPage.getTotal());
    }


    // 出售详情
    public PageResult<SellingDoList> getPurchaseOrderDetail(SellOrderPageReqVO reqVo, Page<ApiOrderDO> page, LoginUser loginUser) throws JsonProcessingException {
        // 下单状态
        List<SellingDoList> sellingDoLists = new ArrayList<>();
        LambdaQueryWrapper<ApiOrderDO> apiOrderDO;
        // 匹配订单状态
        if(reqVo.getTransferStatus() != null){
            apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                    .eq(ApiOrderDO::getBuyUserId, loginUser.getId())
                    .eq(ApiOrderDO::getBuyUserType, loginUser.getUserType())
                    .eq(ApiOrderDO::getTransferStatus, reqVo.getTransferStatus())
                    .orderByDesc(ApiOrderDO::getCreateTime);
        }else{
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                    .eq(ApiOrderDO::getBuyUserId, loginUser.getId())
                    .eq(ApiOrderDO::getBuyUserType, loginUser.getUserType())
                    .in(ApiOrderDO::getTransferStatus, statusesToMatch)
                    .orderByDesc(ApiOrderDO::getCreateTime);
        }

        // 执行分页查询
        IPage<ApiOrderDO> apiOrderDOIPage = apiOrderMapper.selectPage(page, apiOrderDO);

        // 判断
        if (apiOrderDO.isEmptyOfWhere()) {
            return new PageResult<>(sellingDoLists, 0L);
        }
        // 遍历订单，返回订单号等关键数据
        for (ApiOrderDO apiOrderDO1 : apiOrderDOIPage.getRecords()) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(apiOrderDO1.getOrderNo());
            sellingDoListTemp.setCommodityAmount(apiOrderDO1.getPayAmount());
            sellingDoListTemp.setMerchantNo(apiOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(apiOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(apiOrderDO1.getTransferStatus());

            ApiQueryCommodityReqVo apiQueryCommodityReqVo = apiOrderDO1.getBuyInfo();

            if(apiQueryCommodityReqVo != null){
                new ApiQueryCommodityReqVo();
            }

            InvPreviewDO invPreviewDOS = invPreviewMapper.selectOne(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eqIfPresent(InvPreviewDO::getMarketHashName, apiQueryCommodityReqVo.getCommodityHashName()));

            if(invPreviewDOS == null){
                invPreviewDOS = new InvPreviewDO();
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS;
            if (matchingInvPreviewDO != null) {
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            } else {
                sellingDoListTemp.setSelExteriorColor((invPreviewDOS.getItemInfo()).getExteriorColor());
            }
            sellingDoLists.add(sellingDoListTemp);
        }
        return new PageResult<>(sellingDoLists, apiOrderDOIPage.getTotal());
    }

    /**
     * @param reqVO
     * @return 成交记录查询
     */
    public List<SellingDO> getSoldInfo(@RequestBody @Valid SellingPageReqVO reqVO) {
        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eqIfPresent(SellingDO::getMarketHashName, reqVO.getMarketHashName())
                .eqIfPresent(SellingDO::getMarketName, reqVO.getMarketName())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.TransferFINISH.getStatus())
                .last("limit 10"));
        ArrayList<SellingDO> list = new ArrayList<>();
        if(!sellingDOS.isEmpty()){
            for (SellingDO aDo : sellingDOS) {
                SellingDO sellingDO = new SellingDO();
                sellingDO.setMarketHashName(aDo.getMarketHashName());
                sellingDO.setMarketName(aDo.getMarketName());
                // 价格
                sellingDO.setPrice(aDo.getPrice());
                // 出售时间
                sellingDO.setUpdateTime(aDo.getUpdateTime());
                // 图片
                sellingDO.setIconUrl(aDo.getIconUrl());
                list.add(sellingDO);
            }
        }
        return list;
    }
}
