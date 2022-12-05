package cn.iocoder.yudao.module.trade.controller.app.cart.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(title = "用户 App - 购物车更新数量 Request VO")
@Data
public class AppTradeCartItemUpdateCountReqVO {

    @Schema(title  = "商品 SKU 编号", required = true, example = "1024")
    @NotNull(message = "商品 SKU 编号不能为空")
    private Long skuId;

    @Schema(title  = "商品数量", required = true, example = "1")
    @NotNull(message = "数量不能为空")
    @Min(message = "数量必须大于 0", value = 1L)
    private Integer count;

}
