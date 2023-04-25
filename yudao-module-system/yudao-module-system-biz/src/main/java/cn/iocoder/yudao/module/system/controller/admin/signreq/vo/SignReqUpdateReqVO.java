package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 注册申请更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SignReqUpdateReqVO extends SignReqBaseVO {

    @Schema(description = "申请id,主键(自增策略)", required = true, example = "533")
    @NotNull(message = "申请id,主键(自增策略)不能为空")
    private Long id;

}
