package cn.iocoder.yudao.module.oa.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 产品反馈更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeedbackUpdateReqVO extends FeedbackBaseVO {

    @Schema(description = "id", required = true, example = "15188")
    @NotNull(message = "id不能为空")
    private Long id;

}
