package cn.iocoder.yudao.framework.web.config;

import cn.iocoder.yudao.framework.web.core.error.YudaoHandlerExceptionResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.List;

/**
 * @author suyh
 * @since 2023-09-14
 */
public class YudaoWebMvcConfigurer implements WebMvcConfigurer {
    private final WebProperties webProperties;

    public YudaoWebMvcConfigurer(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurePathMatch(configurer, webProperties.getAdminApi());
        configurePathMatch(configurer, webProperties.getAppApi());
    }

    /**
     * 设置 API 前缀，仅仅匹配 controller 包下的
     *
     * @param configurer 配置
     * @param api        API 配置
     */
    private void configurePathMatch(PathMatchConfigurer configurer, WebProperties.Api api) {
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(api.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match(api.getController(), clazz.getPackage().getName())); // 仅仅匹配 controller 包
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 将spring mvc 创建的DefaultHandlerExceptionResolver 删除掉，使用自定义的DefaultHandlerExceptionResolver 派生类替代
        // 自定义的异常处理类 SupplyHandlerExceptionResolver 派生自默认异常处理类的，所以拥有它的一切功能，只是我们添加了一些扩展。
        for (int i = 0; i < resolvers.size(); i++) {
            HandlerExceptionResolver resolver = resolvers.get(i);
            if (DefaultHandlerExceptionResolver.class.isAssignableFrom(resolver.getClass())) {
                resolvers.remove(i);
                break;
            }
        }

        resolvers.add(new YudaoHandlerExceptionResolver());
    }

}
