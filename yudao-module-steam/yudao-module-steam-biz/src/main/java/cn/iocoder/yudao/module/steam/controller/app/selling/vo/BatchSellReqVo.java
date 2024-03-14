package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class BatchSellReqVo {
    @Valid
    @NotNull(message = "库存不能为空，至少为一个库存")
    @NotEmpty(message = "库存不能为空，至少为一个库存")
    private List<Item> items;
    @Data
    public static class Item{
        @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
        @NotNull(message = "库存不能为空")
        private Long id;

        @Schema(description = "出售价格单价", example = "3557")
        @NotNull(message = "出售价格单价不能为空")
        private Integer price;
    }
}
