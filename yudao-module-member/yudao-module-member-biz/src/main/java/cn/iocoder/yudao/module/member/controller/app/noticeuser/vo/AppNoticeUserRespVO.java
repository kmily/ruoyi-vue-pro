package cn.iocoder.yudao.module.member.controller.app.noticeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 用户消息关联 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppNoticeUserRespVO extends AppNoticeUserBaseVO {

    @Schema(description = "主键", required = true, example = "4919")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
