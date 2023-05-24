package cn.iocoder.yudao.module.oa.dal.dataobject.attendance;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.oa.enums.attendance.AttendancePeriodEnum;
import cn.iocoder.yudao.module.oa.enums.attendance.AttendanceTypeEnum;
import cn.iocoder.yudao.module.oa.enums.attendance.VisitCustomerTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 考勤打卡 DO
 *
 * @author 管理员
 */
@TableName("oa_attendance")
@KeySequence("oa_attendance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 打卡类型
     *
     * 枚举 {@link AttendancePeriodEnum 对应的类}
     */
    private int attendanceType;
    /**
     * 打卡时间段
     *
     * 枚举 {@link AttendanceTypeEnum 对应的类}
     */
    private String attendancePeriod;
    /**
     * 工作内容
     */
    private String workContent;
    /**
     * 拜访客户id
     */
    private Long customerId;
    /**
     * 拜访类型
     *
     * 枚举 {@link VisitCustomerTypeEnum 对应的类}
     */
    private String visitType;
    /**
     * 拜访事由
     */
    private String visitReason;
    /**
     * 请假开始时间
     */
    private LocalDateTime leaveBeginTime;
    /**
     * 请假结束日期
     */
    private LocalDateTime leaveEndTime;
    /**
     * 请假事由
     */
    private String leaveReason;
    /**
     * 请假工作交接
     */
    private String leaveHandover;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
