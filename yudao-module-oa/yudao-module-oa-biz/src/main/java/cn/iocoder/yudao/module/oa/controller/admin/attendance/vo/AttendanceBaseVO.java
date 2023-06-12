package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 考勤打卡 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AttendanceBaseVO {

    @Schema(description = "打卡类型", example = "1")
    private Byte attendanceType;

    @Schema(description = "打卡时间段")
    private Byte attendancePeriod;

    @Schema(description = "工作内容")
    private String workContent;

    @Schema(description = "打卡地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "拜访客户id", example = "11620")
    private Long customerId;

    @Schema(description = "拜访类型", example = "1")
    private Byte visitType;

    @Schema(description = "拜访事由", example = "不喜欢")
    private String visitReason;

    @Schema(description = "请假开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime leaveBeginTime;

    @Schema(description = "请假结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime leaveEndTime;

    @Schema(description = "请假事由", example = "不喜欢")
    private String leaveReason;

    @Schema(description = "请假工作交接")
    private String leaveHandover;

}
