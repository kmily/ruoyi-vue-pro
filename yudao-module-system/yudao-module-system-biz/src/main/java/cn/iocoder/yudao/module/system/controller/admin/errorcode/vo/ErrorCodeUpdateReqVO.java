package cn.iocoder.yudao.module.system.controller.admin.errorcode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 错误码更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorCodeUpdateReqVO extends ErrorCodeBaseVO {

    @Schema(description = "错误码编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "错误码编号不能为空")
    private Long id;

}
