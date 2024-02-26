package cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 品质选择新增/修改 Request VO")
@Data
public class SelRaritySaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2155")
    private Long id;

    @Schema(description = "中文名称", example = "赵六")
    @NotEmpty(message = "中文不能为空")
    private String localizedTagName;

    @Schema(description = "英文名称", example = "王五")
    @NotEmpty(message = "英文不能为空")
    private String internalName;

    @Schema(description = "色彩", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;

}