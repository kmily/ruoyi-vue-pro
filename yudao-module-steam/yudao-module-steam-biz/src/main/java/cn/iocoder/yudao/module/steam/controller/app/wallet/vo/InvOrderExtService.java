package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
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
                    InvTransferStatusEnum.TransferFINISH.getStatus(),
                    InvTransferStatusEnum.INORDER.getStatus(),
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
}
