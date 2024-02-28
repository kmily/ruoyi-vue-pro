package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class InvPageReqVo {


    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
    @NotNull(message = "Primary Key")
    private Long id;

    @Schema(description = "出售价格单价", example = "3557")
    @NotNull(message = "出售价格单价")
    private Integer price;


}
