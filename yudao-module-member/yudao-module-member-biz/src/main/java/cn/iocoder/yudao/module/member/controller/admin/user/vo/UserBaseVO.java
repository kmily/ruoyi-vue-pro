package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 会员 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class UserBaseVO {

    @Schema(description = "用户昵称", required = true, example = "张三")
    @NotNull(message = "用户昵称不能为空")
    private String nickname;

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

    @Schema(description = "性别")
    private Byte sex;

    @Schema(description = "出生日期")
    private String birthday;

    @Schema(description = "注册 IP", required = true)
    @NotNull(message = "注册 IP不能为空")
    private String registerIp;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime loginDate;

}
