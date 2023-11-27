package cn.iocoder.yudao.module.member.controller.app.serverperson.vo;

import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.validation.IDCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 被服务人 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AppServerPersonBaseVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1470")
    @NotNull(message = "用户编号不能为空")
    private Long memberId;

    @Schema(description = "被服务人姓名", example = "赵六")
    private String name;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证号不能为空")
    @IDCard
    private String idCard;

    @Schema(description = "联系方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "联系方式不能为空")
    private String mobile;

    @Schema(description = "出生日期", deprecated = true)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    private LocalDate birthday;

    @Schema(description = "用户性别", deprecated = true)
    private Byte sex;

    @Schema(description = "紧急联系人")
    private String critical;

    @Schema(description = "是否实名 NO-未实名，YES-实名", example = "李四")
    private String realname;

    @Schema(description = "医保卡正面")
    private String medicalCardFront;

    @Schema(description = "医保卡反面")
    private String medicalCardBack;

    @Schema(description = "病历资料路径 [xxx, xxx]")
    private  List<String> medicalRecord;

    @Schema(description = "特殊情况路径[xxx，xxxx]")
    private  List<String> special;

    @Schema(description = "状态", example = "1", deprecated = true)
    private String status;

    @Schema(description = "最终审核人", example = "张三", deprecated = true)
    private String checkName;

    @Schema(description = "最终审核时间", deprecated = true)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime checkTime;

    @Schema(description = "审核意见", deprecated = true)
    private String opinion;

}
