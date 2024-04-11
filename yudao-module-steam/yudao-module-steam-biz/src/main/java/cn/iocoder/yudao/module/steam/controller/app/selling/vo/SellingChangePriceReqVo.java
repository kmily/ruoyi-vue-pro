package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class SellingChangePriceReqVo {

        @Schema(description = "Selling表主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
        @NotNull(message = "Primary Key")
        private Long id;
        @Schema(description = "价格表主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
        @NotNull(message = "价格不能为空")
        private Integer price;
}