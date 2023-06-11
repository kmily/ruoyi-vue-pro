package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户 Excel 导出 Request VO，参数和 MemberUserPageReqVO 是一致的")
@Data
public class MemberUserExportReqVO {

    @Schema(description = "用户昵称", example = "芋艿")
    private String nickname;

    @Schema(description = "用户真实名称", example = "张三")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "状态", example = "1")
    private Byte status;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "注册 IP")
    private String registerIp;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "支付密码")
    private String payPassword;

    @Schema(description = "最后登录时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] loginDate;

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
