package cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 人体检测雷达设置 DO
 *
 * @author 芋道源码
 */
@TableName("member_detection_alarm_settings")
@KeySequence("member_detection_alarm_settings_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionAlarmSettingsDO extends BaseDO {

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
     * 进入告警
     */
    private Boolean enter;
    /**
     * 离开告警
     */
    private Boolean goOut;

}
