package cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 收藏品选择新增/修改 Request VO")
@Data
public class SelItemsetSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3534")
    private Long id;

    @Schema(description = "父级编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15350")
    @NotNull(message = "父级编号不能为空")
    private Long parentId;

    @Schema(description = "英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "英文名称不能为空")
    private String internalName;

    @Schema(description = "中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "中文名称不能为空")
    private String localizedTagName;

}