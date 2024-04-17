package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.SellOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
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
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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



    public PageResult<SellingDoList> getSellOrderWithPage(InvOrderPageReqVO reqVo, Page<InvOrderDO> page, LoginUser loginUser) {
        // 下单状态
        List<SellingDoList> sellingDoLists = new ArrayList<>();
        LambdaQueryWrapper<InvOrderDO> invOrderDO;
        // 匹配订单状态
        if(reqVo.getTransferStatus() != null){
            invOrderDO = new LambdaQueryWrapper<InvOrderDO>()
                    .eq(InvOrderDO::getSellUserId, loginUser.getId())
                    .eq(InvOrderDO::getSellUserType, loginUser.getUserType())
                    .eq(InvOrderDO::getTransferStatus, reqVo.getTransferStatus())
                    .orderByDesc(InvOrderDO::getCreateTime);
        }else{
            List<Integer> statusesToMatch = Arrays.asList(
                    InvTransferStatusEnum.INORDER.getStatus(),
                    InvTransferStatusEnum.TransferING.getStatus(),
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.OFF_SALE.getStatus(),
                    InvTransferStatusEnum.TransferERROR.getStatus(),
                    InvTransferStatusEnum.CLOSE.getStatus());
            invOrderDO = new LambdaQueryWrapper<InvOrderDO>()
                    .eq(InvOrderDO::getSellUserId, loginUser.getId())
                    .eq(InvOrderDO::getSellUserType, loginUser.getUserType())
                    .in(InvOrderDO::getTransferStatus, statusesToMatch)
                    .orderByDesc(InvOrderDO::getCreateTime);
        }

        // 执行分页查询
        IPage<InvOrderDO> invOrderPage = invOrderMapper.selectPage(page, invOrderDO);

        // 判断
        if (invOrderDO.isEmptyOfWhere()) {
            return new PageResult<>(sellingDoLists, 0L);
        }
        // 遍历订单，返回订单号等关键数据
        for (InvOrderDO invOrderDOTemp : invOrderPage.getRecords()) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(invOrderDOTemp.getOrderNo());
            sellingDoListTemp.setPayTime(invOrderDOTemp.getPayTime());
            sellingDoListTemp.setCommodityAmount(invOrderDOTemp.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(invOrderDOTemp.getMerchantNo());
            sellingDoListTemp.setMarketName(invOrderDOTemp.getMarketName());
            sellingDoListTemp.setCreateTime(invOrderDOTemp.getCreateTime());
            sellingDoListTemp.setTransferStatus(invOrderDOTemp.getTransferStatus());

            List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eq(InvPreviewDO::getItemName, invOrderDOTemp.getMarketName()));
            if(invPreviewDOS.isEmpty()){
                invPreviewDOS.add(new InvPreviewDO());
            }
            sellingDoListTemp.setSelExterior(invPreviewDOS.get(0).getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.get(0).getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.get(0).getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.get(0).getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS.stream()
                    .filter(invPreview -> invPreview.getMarketHashName().equals(invPreviewDOS.get(0).getMarketHashName()))
                    .findFirst()
                    .orElse(null);
            if (matchingInvPreviewDO != null) {
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            } else {
                sellingDoListTemp.setSelExteriorColor((invPreviewDOS.get(0).getItemInfo()).getExteriorColor());
            }
            sellingDoLists.add(sellingDoListTemp);
        }
        return new PageResult<>(sellingDoLists, invOrderPage.getTotal());
    }


    public PageResult<SellingDoList> getSellOrder(SellOrderPageReqVO reqVo, Page<ApiOrderDO> page, LoginUser loginUser) {
        // 下单状态
        List<SellingDoList> sellingDoLists = new ArrayList<>();
        LambdaQueryWrapper<ApiOrderDO> apiOrderDO;
        // 匹配订单状态
        if(reqVo.getTransferStatus() != null){
            apiOrderDO = new LambdaQueryWrapper<ApiOrderDO>()
                    .eq(ApiOrderDO::getSellUserId, loginUser.getId())
                    .eq(ApiOrderDO::getSellUserType, loginUser.getUserType())
                    .eq(ApiOrderDO::getTransferStatus, reqVo.getTransferStatus())
                    .orderByDesc(ApiOrderDO::getUpdateTime);
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
                    .orderByDesc(ApiOrderDO::getUpdateTime);
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
            sellingDoListTemp.setPayTime(apiOrderDO1.getUpdateTime());
            sellingDoListTemp.setCommodityAmount(apiOrderDO1.getCommodityAmount());
            sellingDoListTemp.setMerchantNo(apiOrderDO1.getMerchantNo());
            sellingDoListTemp.setCreateTime(apiOrderDO1.getCreateTime());
            sellingDoListTemp.setTransferStatus(apiOrderDO1.getTransferStatus());

            List<ApiOrderExtDO> apiOrderExtDOS = apiOrderExtMapper.selectList(new LambdaQueryWrapperX<ApiOrderExtDO>()
                    .eq(ApiOrderExtDO::getOrderId, apiOrderDO1.getId()));

            if(apiOrderExtDOS.isEmpty()){
                apiOrderExtDOS.add(new ApiOrderExtDO());
            }

            // 查找selling表返回相应字段
            List<SellingDO> apiOrderExtDOList = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getId,apiOrderExtDOS.get(0).getCommodityInfo()));
            if(apiOrderExtDOS.isEmpty()){
                apiOrderExtDOList.add(new SellingDO());
            }

            List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eq(InvPreviewDO::getMarketHashName, apiOrderExtDOList.get(0).getMarketHashName()));
            if(apiOrderExtDOS.isEmpty()){
                invPreviewDOS.add(new InvPreviewDO());
            }

            sellingDoListTemp.setSelExterior(invPreviewDOS.get(0).getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.get(0).getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.get(0).getItemName());
            sellingDoListTemp.setMarketHashName(invPreviewDOS.get(0).getMarketHashName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS.stream()
                    .filter(invPreview -> invPreview.getMarketHashName().equals(invPreviewDOS.get(0).getMarketHashName()))
                    .findFirst()
                    .orElse(null);
            if (matchingInvPreviewDO != null) {
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            } else {
                sellingDoListTemp.setSelExteriorColor((invPreviewDOS.get(0).getItemInfo()).getExteriorColor());
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
