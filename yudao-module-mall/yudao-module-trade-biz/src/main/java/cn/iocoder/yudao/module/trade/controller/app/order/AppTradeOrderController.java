package cn.iocoder.yudao.module.trade.controller.app.order;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderGetCreateInfoRespVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.TradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.TradeOrderRespVO;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户 App - 交易订单")
@RestController
@RequestMapping("/trade/order")
@RequiredArgsConstructor // TODO @LeeYan9: 先统一使用 @Resource 注入哈; 项目只有三层, 依赖注入会存在, 所以使用 @Resource; 也因此, 最好全局保持一致
@Validated
@Slf4j
public class AppTradeOrderController {

    private final TradeOrderService tradeOrderService;

    @GetMapping("/get-create-info")
    @Operation(summary = "基于商品，确认创建订单")
    @PreAuthenticated
    public CommonResult<AppTradeOrderGetCreateInfoRespVO> getTradeOrderCreateInfo(AppTradeOrderCreateReqVO createReqVO) {
//        return success(tradeOrderService.getOrderConfirmCreateInfo(UserSecurityContextHolder.getUserId(), skuId, quantity, couponCardId));
        return null;
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @PreAuthenticated
    public CommonResult<Long> createTradeOrder(@RequestBody AppTradeOrderCreateReqVO createReqVO,
                                               HttpServletRequest servletRequest) {
        // 获取登录用户、用户 IP 地址
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String clientIp = JakartaServletUtil.getClientIP(servletRequest);
        // 创建交易订单，预支付记录
        Long orderId = tradeOrderService.createTradeOrder(loginUserId, clientIp, createReqVO);
        return CommonResult.success(orderId);
    }

    @GetMapping("/get")
    @Operation(summary = "获得交易订单")
    public CommonResult<TradeOrderRespVO> getTradeOrder(@RequestParam("tradeOrderId") Integer tradeOrderId) {
//        return success(tradeOrderService.getTradeOrder(tradeOrderId));
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单交易分页")
    public CommonResult<PageResult<TradeOrderRespVO>> pageTradeOrder(TradeOrderPageReqVO pageVO) {
//        return success(tradeOrderService.pageTradeOrder(UserSecurityContextHolder.getUserId(), pageVO));
        return null;
    }

}
