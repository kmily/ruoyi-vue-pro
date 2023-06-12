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

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 考勤打卡 Excel VO
 *
 * @author 东海
 */
@Data
public class AttendanceExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("打卡类型")
    private Byte attendanceType;

    @ExcelProperty("打卡时间段")
    private Byte attendancePeriod;

    @ExcelProperty("工作内容")
    private String workContent;

    @ExcelProperty("打卡地址")
    private String address;

    @ExcelProperty("经度")
    private BigDecimal longitude;

    @ExcelProperty("纬度")
    private BigDecimal latitude;

    @ExcelProperty("拜访客户id")
    private Long customerId;

    @ExcelProperty("拜访类型")
    private Byte visitType;

    @ExcelProperty("拜访事由")
    private String visitReason;

    @ExcelProperty("请假开始时间")
    private LocalDateTime leaveBeginTime;

    @ExcelProperty("请假结束时间")
    private LocalDateTime leaveEndTime;

    @ExcelProperty("请假事由")
    private String leaveReason;

    @ExcelProperty("请假工作交接")
    private String leaveHandover;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
