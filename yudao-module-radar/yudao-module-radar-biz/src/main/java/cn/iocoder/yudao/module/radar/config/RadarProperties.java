package cn.iocoder.yudao.module.radar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whycode
 * @title: RadarProperties
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/213:48
 */

@Data
@ConfigurationProperties(prefix = "yudao.radar.socket")
public class RadarProperties {

    /**
     * socket端口
     */
    private int port;

    /**
     * socket 地址
     */
    private String host;

}
