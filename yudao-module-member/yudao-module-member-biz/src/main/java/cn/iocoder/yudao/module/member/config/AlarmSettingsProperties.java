package cn.iocoder.yudao.module.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author whycode
 * @title: AlarmSettingsProperties
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/79:03
 */
@Data
@ConfigurationProperties(prefix = "yudao.member.alarm")
public class AlarmSettingsProperties {


    private String heartRange;

    private String breatheRange;

}
