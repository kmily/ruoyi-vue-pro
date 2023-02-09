package cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * mqtt主题 DO
 *
 * @author ZanGe
 */
@TableName("system_mqtt_topic")
@KeySequence("system_mqtt_topic_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttTopicDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 主题名称
     */
    private String topicName;
    /**
     * 订阅主题
     */
    private String subTopic;
    /**
     * 发布主题
     */
    private String pusTopic;
    /**
     * 状态0启用1未启用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
