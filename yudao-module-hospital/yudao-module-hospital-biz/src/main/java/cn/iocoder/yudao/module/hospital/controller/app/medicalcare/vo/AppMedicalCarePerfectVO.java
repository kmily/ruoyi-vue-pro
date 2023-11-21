package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import cn.iocoder.yudao.framework.common.validation.IDCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author whycode
 * @title: AppMedicalCarePerfectVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2114:50
 */
@Schema(description = "APP - 医护管理更新 Request VO")
@Data
@ToString(callSuper = true)
public class AppMedicalCarePerfectVO {

    @Schema(description = "医护编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证号不能为空")
    @IDCard(message = "身份证号格式不正确")
    private String idCard;

    @Schema(description = "出生日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate birthday;

    @Schema(description = "用户性别")
    private Byte sex;

    @Schema(description = "紧急联系人")
    private String critical;

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

    @Schema(description = "个人简介")
    private String introduction;

    @Schema(description = "头像路径")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
}
