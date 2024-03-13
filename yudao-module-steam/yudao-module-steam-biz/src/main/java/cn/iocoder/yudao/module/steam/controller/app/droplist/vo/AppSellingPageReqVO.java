package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppSellingPageReqVO extends PageParam {
    @Schema(description = "marketHashName", example = "王五")
    @NotNull(message = "商品类型不能为空")
    private String marketHashName;
    @Schema(description = "最高价格", example = "2")
    private Integer maxPrice;
    @Schema(description = "最低价格", example = "1")
    private Integer minPrice;
}
