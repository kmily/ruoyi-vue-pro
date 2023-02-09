package cn.iocoder.yudao.module.system.api.mqtttopic.dto;

import lombok.Data;

/**
 * @author ZanGe
 * @create 2023/2/2 22:33
 */
@Data
public class MqttTopicRespDTO {
    /**
     * 编号
     */
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
