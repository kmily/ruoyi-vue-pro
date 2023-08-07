package cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 体征检测雷达设置 DO
 *
 * @author 芋道源码
 */
@TableName("member_health_alarm_settings")
@KeySequence("member_health_alarm_settings_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthAlarmSettingsDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知
     */
    private Byte notice;
    /**
     * 心率异常告警
     */
    private Boolean heart;
    /**
     * 正常心率范围
     */
    private String heartRange;
    /**
     * 呼吸告警设置
     */
    private Boolean breathe;
    /**
     * 正常心率范围 [16,24]
     */
    private String breatheRange;
    /**
     * 体动检测告警
     */
    private Boolean move;
    /**
     * 坐起告警
     */
    private Boolean sitUp;
    /**
     * 离床告警
     */
    private Boolean outBed;

}
