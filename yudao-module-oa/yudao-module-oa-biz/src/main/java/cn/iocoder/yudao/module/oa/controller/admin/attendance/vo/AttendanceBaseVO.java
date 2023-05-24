package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.module.oa.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 考勤打卡 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AttendanceBaseVO {

    @Schema(description = "打卡类型", required = true, example = "1")
    @NotNull(message = "打卡类型不能为空")
    @DictFormat(DictTypeConstants.OA_ATTENDANCE_TYPE)
    private int attendanceType;

    @Schema(description = "打卡时间段")
    private String attendancePeriod;

    @Schema(description = "工作内容")
    private String workContent;

    @Schema(description = "拜访客户id", example = "25439")
    private Long customerId;

    @Schema(description = "拜访类型", example = "2")
    private String visitType;

    @Schema(description = "拜访事由", example = "不香")
    private String visitReason;

    @Schema(description = "请假开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime leaveBeginTime;

    @Schema(description = "请假结束日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime leaveEndTime;

    @Schema(description = "请假事由", example = "不香")
    private String leaveReason;

    @Schema(description = "请假工作交接")
    private String leaveHandover;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "更新者")
    private String updater;

}
