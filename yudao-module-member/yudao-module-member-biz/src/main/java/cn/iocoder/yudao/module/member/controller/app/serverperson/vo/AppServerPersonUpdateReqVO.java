package cn.iocoder.yudao.module.member.controller.app.serverperson.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 被服务人更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppServerPersonUpdateReqVO extends AppServerPersonBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16294")
    @NotNull(message = "ID不能为空")
    private Long id;

}
