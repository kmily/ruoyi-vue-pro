package cn.iocoder.yudao.framework.desensitization.config;

import cn.iocoder.yudao.framework.desensitization.interceptor.DesensitizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author VampireAchao
 * @since 2022/10/6 14:49
 */
@Configuration
public class YudaoDesensitizationAutoConfiguration {

    @Bean
    public DesensitizationInterceptor desensitizationInterceptor() {
        return new DesensitizationInterceptor();
    }

}
