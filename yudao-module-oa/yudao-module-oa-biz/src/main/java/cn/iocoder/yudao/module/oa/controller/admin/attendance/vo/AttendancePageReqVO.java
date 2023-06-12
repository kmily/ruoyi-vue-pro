package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 考勤打卡分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendancePageReqVO extends PageParam {

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
    private LocalDateTime[] leaveBeginTime;

    @Schema(description = "请假结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] leaveEndTime;

    @Schema(description = "请假事由", example = "不喜欢")
    private String leaveReason;

    @Schema(description = "请假工作交接")
    private String leaveHandover;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
