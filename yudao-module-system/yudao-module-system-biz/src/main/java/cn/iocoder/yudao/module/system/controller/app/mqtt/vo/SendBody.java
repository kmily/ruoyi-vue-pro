package cn.iocoder.yudao.module.system.controller.app.mqtt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送的消息体
 *
 * @author Hermit
 * @date 2021/11/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendBody {

    /**
     * 主体
     */
    private String topic;
    /**
     * 服务质量
     */
    private Integer qos;
    /**
     * 消息体
     */
    private String pushMessage;
    /**
     * 是否保留
     */
    private boolean retained;
}