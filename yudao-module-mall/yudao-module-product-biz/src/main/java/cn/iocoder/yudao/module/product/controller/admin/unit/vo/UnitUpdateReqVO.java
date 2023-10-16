package cn.iocoder.yudao.module.product.controller.admin.unit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 计量单位更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnitUpdateReqVO extends UnitBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2202")
    @NotNull(message = "ID不能为空")
    private Long id;

}
