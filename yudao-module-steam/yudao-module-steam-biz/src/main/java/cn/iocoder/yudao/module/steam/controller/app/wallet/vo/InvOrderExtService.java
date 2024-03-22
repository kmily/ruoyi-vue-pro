package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.controller.app.wallet.vo.SellingDoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Slf4j
public class InvOrderExtService {
    @Resource
    private InvOrderMapper invOrderMapper;
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private InvPreviewMapper invPreviewMapper;

    public CommonResult<List<SellingDoList>> getSellOrderWithPage(QueryOrderReqVo reqVo, LoginUser loginUser) {
        List<InvOrderDO> invOrderDO = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getSellUserId, loginUser.getId())
                .eq(InvOrderDO::getSellUserType, loginUser.getUserType()));
        // 返回图片 外观 颜色
        List<SellingDO> sellingDO = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType()));

        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eq(InvPreviewDO::getMarketHashName, sellingDO.get(0).getMarketHashName()));

        List<SellingDoList> sellingDoLists = new ArrayList<>();

        for (InvOrderDO invOrderDOTemp : invOrderDO) {
            for (SellingDO sellingDOTemp : sellingDO) {
                SellingDoList sellingDoListTemp = new SellingDoList();
                sellingDoListTemp.setSelExterior(sellingDOTemp.getSelExterior());
                sellingDoListTemp.setIconUrl(sellingDOTemp.getIconUrl());
                sellingDoListTemp.setOrderNo(invOrderDOTemp.getOrderNo());
                sellingDoListTemp.setPaymentAmount(invOrderDOTemp.getPaymentAmount());
                sellingDoListTemp.setCreateTime(invOrderDOTemp.getCreateTime());
                sellingDoListTemp.setMerchantNo(invOrderDOTemp.getMerchantNo());
                sellingDoListTemp.setMarketName(sellingDOTemp.getMarketName());
                sellingDoListTemp.setTransferStatus(sellingDOTemp.getTransferStatus());
                InvPreviewDO matchingInvPreviewDO = invPreviewDOS.stream()
                        .filter(invPreview -> invPreview.getMarketHashName().equals(sellingDOTemp.getMarketHashName()))
                        .findFirst()
                        .orElse(null);
                if (matchingInvPreviewDO != null) {
                    sellingDoListTemp.setSelExteriorColor(matchingInvPreviewDO.getItemInfo().getExteriorColor());
                } else {
                    sellingDoListTemp.setSelExteriorColor((invPreviewDOS.get(0).getItemInfo()).getExteriorColor());
                }
                sellingDoLists.add(sellingDoListTemp);
            }
        }
        return CommonResult.success(sellingDoLists);
    }
}
