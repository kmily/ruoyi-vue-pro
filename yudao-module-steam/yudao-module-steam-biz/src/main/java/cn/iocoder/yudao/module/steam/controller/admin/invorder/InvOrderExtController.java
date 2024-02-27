package cn.iocoder.yudao.module.steam.controller.admin.invorder;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderRespVO;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.InvOrderSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.invorder.InvOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - steam订单")
@RestController
@RequestMapping("/steam/inv-orderExt")
@Validated
public class InvOrderExtController {

    @Resource
    private PaySteamOrderService paySteamOrderService;
    @Autowired
    private SteamService steamService;
    @Autowired
    private PayOrderService payOrderService;


    @PostMapping("/invOrder/refundOrder")
    @Operation(summary = "steam订单退款")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:refund')")
    public CommonResult<Boolean> refundInvOrder(@RequestParam("id") Long id) {
        InvOrderDO invOrder = paySteamOrderService.getInvOrder(id);
        if(!Objects.nonNull(invOrder)){
            LoginUser loginUser=new LoginUser();
            loginUser.setId(invOrder.getUserId());
            loginUser.setUserType(invOrder.getUserType());
            paySteamOrderService.refundInvOrder(loginUser,id, ServletUtils.getClientIP());
            return success(true);
        }else{
            return error(new ErrorCode(-1,"订单不存在"));
        }
    }

    @PostMapping("/invOrder/tradeAsset")
    @Operation(summary = "steam订单发货")
    @PreAuthorize("@ss.hasPermission('steam:inv-order:refund')")
    public CommonResult<Boolean> tradeAsset(@RequestParam("id") Long id) {
        InvOrderDO invOrder = paySteamOrderService.getInvOrder(id);
        PayOrderDO order = payOrderService.getOrder(invOrder.getPayOrderId());
        if(!PayOrderStatusEnum.isSuccess(order.getStatus())){
            return error(new ErrorCode(-1,"订单未传款"));
        }
        steamService.tradeAsset(invOrder);
        return success(true);
    }
}