package cn.iocoder.yudao.module.system.controller.app.mqtt;

import cn.iocoder.yudao.module.system.controller.app.mqtt.client.MqttOperateClient;
import cn.iocoder.yudao.module.system.controller.app.mqtt.vo.SendBody;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * mqtt-控制器
 *
 * @author Hermit
 * @date 2021/11/18
 */
@Slf4j
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Resource
    private MqttOperateClient mqttOperateClient;

    /**
     * 发送自定义消息内容
     *
     * @param data
     */
    @ApiOperation(value = "发送自定义消息内容", notes = "发送自定义消息内容")
    @GetMapping(value = "/publish")
    public void publish(SendBody data) {
        mqttOperateClient.publish(data.getQos(), data.isRetained(), data.getTopic(), data.getPushMessage());
        JSONObject json = (JSONObject) JSONObject.toJSON(data);
        log.info("----->{}", "mqtt 消息发布，使用默认主题发布:" + json.toJSONString());
    }


    /**
     * 断开客户端连接
     */
    @ApiOperation(value = "断开客户端连接", notes = "断开客户端连接")
    @GetMapping(value = "/disconnect")
    public void disconnect() {
        mqttOperateClient.disconnect();
        log.info("----->{}", "客户端连接已断开");
    }

}