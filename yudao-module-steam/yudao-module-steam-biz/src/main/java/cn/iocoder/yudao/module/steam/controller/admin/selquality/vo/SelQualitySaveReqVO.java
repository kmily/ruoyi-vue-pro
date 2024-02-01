package cn.iocoder.yudao.module.steam.controller.admin.selquality.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.*;

@Schema(description = "管理后台 - 类别选择新增/修改 Request VO")
@Data
public class SelQualitySaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "00001")
    private Long id;

    @Schema(description = "英文名", requiredMode = Schema.RequiredMode.REQUIRED, example = "strange")
    @NotEmpty(message = "英文名不能为空")
    private String internalName;

    @Schema(description = "中文", requiredMode = Schema.RequiredMode.REQUIRED, example = "StatTrak™")
    @NotEmpty(message = "中文不能为空")
    private String localizedTagName;

    @Schema(description = "颜色", example = "CF6A32")
    private String color;

}