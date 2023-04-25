package cn.iocoder.yudao.module.oa.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品反馈 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeedbackRespVO extends FeedbackBaseVO {

    @Schema(description = "id", required = true, example = "15188")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
