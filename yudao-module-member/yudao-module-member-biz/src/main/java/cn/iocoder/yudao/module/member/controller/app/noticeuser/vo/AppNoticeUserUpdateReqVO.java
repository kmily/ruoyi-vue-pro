package cn.iocoder.yudao.module.member.controller.app.noticeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 用户消息关联更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppNoticeUserUpdateReqVO extends AppNoticeUserBaseVO {

    @Schema(description = "主键", required = true, example = "4919")
    @NotNull(message = "主键不能为空")
    private Long id;

}
