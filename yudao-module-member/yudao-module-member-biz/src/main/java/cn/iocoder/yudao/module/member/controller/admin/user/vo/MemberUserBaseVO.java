package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 用户 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class MemberUserBaseVO {

    @Schema(description = "用户昵称", required = true, example = "芋艿")
    @NotNull(message = "用户昵称不能为空")
    private String nickname;

    @Schema(description = "用户真实名称", required = true, example = "张三")
    @NotNull(message = "用户真实名称不能为空")
    private String realName;

    @Schema(description = "头像", required = true)
    @NotNull(message = "头像不能为空")
    private String avatar;

    @Schema(description = "状态", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Byte status;

    @Schema(description = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(description = "注册 IP", required = true)
    @NotNull(message = "注册 IP不能为空")
    private String registerIp;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "支付密码")
    private String payPassword;

    @Schema(description = "最后登录时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime loginDate;

    @Schema(description = "区域id", example = "20859")
    private Long areaId;

    @Schema(description = "用户组id", example = "12006")
    private Long userGroupId;

    @Schema(description = "积分")
    private Long point;

    @Schema(description = "推荐人")
    private Long referrer;

    @Schema(description = "性别")
    private Byte gender;

    @Schema(description = "标签", example = "28893")
    private Long labelId;

}
