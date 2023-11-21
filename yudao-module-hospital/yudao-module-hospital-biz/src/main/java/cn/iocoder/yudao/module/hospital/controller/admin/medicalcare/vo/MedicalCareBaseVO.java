package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import cn.iocoder.yudao.framework.common.validation.IDCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 医护管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class MedicalCareBaseVO {

    @Schema(description = "组织编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5204")
    private Long orgId;

    @Schema(description = "会员编号")
    private Long memberId;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证号不能为空")
    @IDCard(message = "身份证号格式不正确")
    private String idCard;

    @Schema(description = "身份证图片[正,反]")
    private List<String> cardPath;

    @Schema(description = "联系方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "联系方式不能为空")
    private String mobile;

    @Schema(description = "出生日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate birthday;

    @Schema(description = "用户性别")
    private Byte sex;

    @Schema(description = "紧急联系人")
    private String critical;

    @Schema(description = "是否实名 NO-未实名，YES-实名", example = "NO")
    private String realname;

    @Schema(description = "职称")
    private Byte title;

    @Schema(description = "所属机构")
    private String organization;

    @Schema(description = "服务人数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "服务时长，单位分钟", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer timeLength;

    @Schema(description = "从业时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate practiceTime;

    @Schema(description = "主要擅长")
    private Set<String> genius;

    @Schema(description = "是否完善 NO-未实名，YES-实名")
    private String perfect;

    @Schema(description = "是否资质认证 NO-未实名，YES-实名")
    private String aptitude;

    @Schema(description = "个人简介")
    private String introduction;

    @Schema(description = "最终审核状态", example = "1")
    private String status;

    @Schema(description = "最终审核人", example = "王五")
    private String checkName;

    @Schema(description = "最终审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime checkTime;

    @Schema(description = "审核意见")
    private String opinion;

    @Schema(description = "是否是店主")
    private Boolean shopkeeper;

    @Schema(description = "是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper;

    @Schema(description = "是否可以提现", requiredMode = Schema.RequiredMode.REQUIRED)
    private Byte cashOut;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 来源 来源 0-自册, 1-后台添加
     */
    @Schema(description = "来源 SELF-自册, BACK_ADD-后台添加")
    private String source;

    @Schema(description = "头像路径")
    private String avatar;
}
