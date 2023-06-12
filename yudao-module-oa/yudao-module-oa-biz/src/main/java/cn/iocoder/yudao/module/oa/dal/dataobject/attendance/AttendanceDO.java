package cn.iocoder.yudao.module.oa.dal.dataobject.attendance;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 考勤打卡 DO
 *
 * @author 东海
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
     */
    private Byte attendanceType;
    /**
     * 打卡时间段
     */
    private Byte attendancePeriod;
    /**
     * 工作内容
     */
    private String workContent;
    /**
     * 打卡地址
     */
    private String address;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 拜访客户id
     */
    private Long customerId;
    /**
     * 拜访类型
     */
    private Byte visitType;
    /**
     * 拜访事由
     */
    private String visitReason;
    /**
     * 请假开始时间
     */
    private LocalDateTime leaveBeginTime;
    /**
     * 请假结束时间
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

}
