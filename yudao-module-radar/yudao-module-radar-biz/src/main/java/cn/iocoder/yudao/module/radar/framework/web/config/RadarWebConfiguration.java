package cn.iocoder.yudao.module.radar.framework.web.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whycode
 * @title: RadarWebConfiguration
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/716:16
 */
@Configuration(proxyBeanMethods = false)
public class RadarWebConfiguration {

    /**
     * radar 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi radarGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("radar");
    }

}
