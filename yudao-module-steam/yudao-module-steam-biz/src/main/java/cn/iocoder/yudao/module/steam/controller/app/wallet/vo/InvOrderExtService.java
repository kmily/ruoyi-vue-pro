package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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

    public PageResult<SellingDoList> getSellOrderWithPage(QueryOrderReqVo reqVo, LoginUser loginUser) {
        // 下单状态
        List<Integer> statusesToMatch = Arrays.asList(
                InvTransferStatusEnum.TransferFINISH.getStatus(),
                InvTransferStatusEnum.INORDER.getStatus(),
                InvTransferStatusEnum.TransferERROR.getStatus(),
                InvTransferStatusEnum.CLOSE.getStatus(),
                InvTransferStatusEnum.INORDER.getStatus());
        List<SellingDoList> sellingDoLists = new ArrayList<>();

        // 匹配订单状态
        List<InvOrderDO> invOrderDO = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getSellUserId, loginUser.getId())
                .eq(InvOrderDO::getSellUserType, loginUser.getUserType())
                .in(InvOrderDO::getTransferStatus, statusesToMatch)
                .orderByDesc(InvOrderDO::getCreateTime));
        // 判断
        if (invOrderDO.isEmpty()) {
            return new PageResult<>(sellingDoLists, 0L);
        }
        // 遍历订单，返回订单号等关键数据
        for (InvOrderDO invOrderDOTemp : invOrderDO) {
            SellingDoList sellingDoListTemp = new SellingDoList();
            sellingDoListTemp.setOrderNo(invOrderDOTemp.getOrderNo());
            sellingDoListTemp.setPayTime(invOrderDOTemp.getPayTime());
            sellingDoListTemp.setPaymentAmount(invOrderDOTemp.getPaymentAmount());
            sellingDoListTemp.setMerchantNo(invOrderDOTemp.getMerchantNo());
            sellingDoListTemp.setMarketName(invOrderDOTemp.getMarketName());
            sellingDoListTemp.setCreateTime(invOrderDOTemp.getCreateTime());

            List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .eq(InvPreviewDO::getItemName, invOrderDOTemp.getMarketName()));
            if(invPreviewDOS.isEmpty()){
                invPreviewDOS.add(new InvPreviewDO());
            }
            // a
            sellingDoListTemp.setSelExterior(invPreviewDOS.get(0).getSelExterior());
            sellingDoListTemp.setIconUrl(invPreviewDOS.get(0).getImageUrl());
            sellingDoListTemp.setMarketName(invPreviewDOS.get(0).getItemName());

            InvPreviewDO matchingInvPreviewDO = invPreviewDOS.stream()
                    .filter(invPreview -> invPreview.getMarketHashName().equals(invPreviewDOS.get(0).getMarketHashName()))
                    .findFirst()
                    .orElse(null);
            if (matchingInvPreviewDO != null) {
                sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
            } else {
                sellingDoListTemp.setSelExteriorColor((invPreviewDOS.get(0).getItemInfo()).getExteriorColor());
            }
            // b
            sellingDoLists.add(sellingDoListTemp);
        }
        return new PageResult<>(sellingDoLists, (long) sellingDoLists.size());
    }
}
