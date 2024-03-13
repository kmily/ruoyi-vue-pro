package cn.iocoder.yudao.module.steam.controller.admin.invorder;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderExtReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - steam订单")
@RestController
@RequestMapping("/steam/inv-orderExt")
@Validated
public class InvOrderExtController {


    private PaySteamOrderService paySteamOrderService;

    private SteamService steamService;

    private PayOrderService payOrderService;
    @Autowired
    public void setPaySteamOrderService(PaySteamOrderService paySteamOrderService) {
        this.paySteamOrderService = paySteamOrderService;
    }
    @Autowired
    public void setSteamService(SteamService steamService) {
        this.steamService = steamService;
    }
    @Autowired
    public void setPayOrderService(PayOrderService payOrderService) {
        this.payOrderService = payOrderService;
    }

    @PostMapping("/invOrder/refundOrder")
    @Operation(summary = "steam订单退款")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:refund')")
    public CommonResult<Boolean> refundInvOrder(@Valid @RequestBody InvOrderExtReqVo invOrderExtReqVo) {
        InvOrderDO invOrder = paySteamOrderService.getInvOrder(invOrderExtReqVo.getId());
        if(Objects.nonNull(invOrder)){
            LoginUser loginUser=new LoginUser();
            loginUser.setId(invOrder.getUserId());
            loginUser.setUserType(invOrder.getUserType());
            paySteamOrderService.refundInvOrder(loginUser,invOrderExtReqVo.getId(), ServletUtils.getClientIP());
            return success(true);
        }else{
            return error(new ErrorCode(-1,"订单不存在"));
        }
    }

    @PostMapping("/invOrder/tradeAsset")
    @Operation(summary = "steam订单发货")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:refund')")
    public CommonResult<Boolean> tradeAsset(@Valid @RequestBody InvOrderExtReqVo invOrderExtReqVo) {
        InvOrderDO invOrder = paySteamOrderService.getInvOrder(invOrderExtReqVo.getId());
        PayOrderDO order = payOrderService.getOrder(invOrder.getPayOrderId());
        if(!PayOrderStatusEnum.isSuccess(order.getStatus())){
            return error(new ErrorCode(-1,"订单没有支付"));
        }
        steamService.tradeAsset(invOrder);
        return success(true);
    }
}