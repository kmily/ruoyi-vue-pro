package cn.iocoder.yudao.module.infra.framework.security.config;

import cn.iocoder.yudao.framework.security.config.AuthorizeRequestsCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Infra 模块的 Security 配置
 */
@Configuration(proxyBeanMethods = false, value = "infraSecurityConfiguration")
public class SecurityConfiguration {

    @Value("${spring.boot.admin.context-path:''}")
    private String adminSeverContextPath;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean("infraAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer(MvcRequestMatcher.Builder mvc) {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                // Swagger 接口文档
                registry.requestMatchers(mvc.pattern("/v3/api-docs/**")).permitAll()
                        .requestMatchers(mvc.pattern("/swagger-ui.html")).permitAll()
                        .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()
                        .requestMatchers(mvc.pattern("/swagger-resources/**")).anonymous()
                        .requestMatchers(mvc.pattern("/webjars/**")).anonymous()
                        .requestMatchers(mvc.pattern("/*/api-docs")).anonymous();
                // Spring Boot Actuator 的安全配置
                registry.requestMatchers(mvc.pattern("/actuator")).anonymous()
                        .requestMatchers(mvc.pattern("/actuator/**")).anonymous();
                // Druid 监控
                registry.requestMatchers(mvc.pattern("/druid/**")).anonymous();
                // Spring Boot Admin Server 的安全配置
                registry.requestMatchers(mvc.pattern(adminSeverContextPath)).anonymous()
                        .requestMatchers(mvc.pattern(adminSeverContextPath + "/**")).anonymous();
                // 文件读取
                registry.requestMatchers(mvc.pattern(buildAdminApi("/infra/file/*/get/**"))).permitAll();
            }

        };
    }
}
