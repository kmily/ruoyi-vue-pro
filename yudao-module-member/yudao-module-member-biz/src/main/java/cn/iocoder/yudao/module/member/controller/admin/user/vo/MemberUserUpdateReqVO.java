package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberUserUpdateReqVO extends MemberUserBaseVO {

    @Schema(description = "编号", required = true, example = "15691")
    @NotNull(message = "编号不能为空")
    private Long id;

}
