package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 考勤打卡 Excel VO
 *
 * @author 管理员
 */
@Data
public class AttendanceExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty(value = "打卡类型", converter = DictConvert.class)
    @DictFormat("attendance_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private int attendanceType;

    @ExcelProperty(value = "打卡时间段", converter = DictConvert.class)
    @DictFormat("attendance_period") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String attendancePeriod;

    @ExcelProperty("工作内容")
    private String workContent;

    @ExcelProperty("拜访客户id")
    private Long customerId;

    @ExcelProperty(value = "拜访类型", converter = DictConvert.class)
    @DictFormat("visit_customer_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String visitType;

    @ExcelProperty("拜访事由")
    private String visitReason;

    @ExcelProperty("请假开始时间")
    private LocalDateTime leaveBeginTime;

    @ExcelProperty("请假结束日期")
    private LocalDateTime leaveEndTime;

    @ExcelProperty("请假事由")
    private String leaveReason;

    @ExcelProperty("请假工作交接")
    private String leaveHandover;

    @ExcelProperty("创建者")
    private String creator;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updater;

}
