package cn.iocoder.yudao.module.trade.controller.app.cart;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppTradeCartDetailRespVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppTradeCartItemAddCountReqVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppTradeCartItemUpdateCountReqVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppTradeCartItemUpdateSelectedReqVO;
import cn.iocoder.yudao.module.trade.service.cart.TradeCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 购物车")
@RestController
@RequestMapping("/trade/cart")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TradeCartController {

    @Resource
    private TradeCartService cartService;

    @PostMapping("/add-count")
    @Operation(summary = "添加商品到购物车")
    @PreAuthenticated
    public CommonResult<Boolean> addCartItemCount(@Valid @RequestBody AppTradeCartItemAddCountReqVO addCountReqVO) {
        cartService.addCartItemCount(getLoginUserId(), addCountReqVO);
        return success(true);
    }

    @PutMapping("update-count")
    @Operation(summary = "更新购物车商品数量")
    @PreAuthenticated
    public CommonResult<Boolean> updateCartItemQuantity(@Valid @RequestBody AppTradeCartItemUpdateCountReqVO updateCountReqVO) {
        cartService.updateCartItemCount(getLoginUserId(), updateCountReqVO);
        return success(true);
    }

    @PutMapping("update-selected")
    @Operation(summary = "更新购物车商品是否选中")
    @PreAuthenticated
    public CommonResult<Boolean> updateCartItemSelected(@Valid @RequestBody AppTradeCartItemUpdateSelectedReqVO updateSelectedReqVO) {
        cartService.updateCartItemSelected(getLoginUserId(), updateSelectedReqVO);
        // 获得目前购物车明细
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除购物车商品")
    @PreAuthenticated
    public CommonResult<Boolean> deleteCartItem(@RequestParam("skuIds") List<Long> skuIds) {
        cartService.deleteCartItems(getLoginUserId(), skuIds);
        return success(true);
    }

    @GetMapping("get-count")
    @Operation(summary = "查询用户在购物车中的商品数量")
    @PreAuthenticated
    public CommonResult<Integer> getCartCount() {
        return success(cartService.getCartCount(getLoginUserId()));
    }

    @GetMapping("/get-detail")
    @Operation(summary = "查询用户的购物车的详情")
    @PreAuthenticated
    public CommonResult<AppTradeCartDetailRespVO> getCartDetail() {
        return success(cartService.getCartDetail(getLoginUserId()));
    }

}
