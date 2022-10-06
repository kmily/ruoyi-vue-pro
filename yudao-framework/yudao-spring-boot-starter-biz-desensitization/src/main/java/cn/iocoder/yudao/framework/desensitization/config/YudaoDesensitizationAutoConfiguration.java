package cn.iocoder.yudao.framework.desensitization.config;

import cn.iocoder.yudao.framework.desensitization.interceptor.DesensitizationInterceptor;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author VampireAchao
 * @since 2022/10/6 14:49
 */
@Configuration
public class YudaoDesensitizationAutoConfiguration {

    @Bean
    public DesensitizationInterceptor desensitizationInterceptor(MybatisPlusInterceptor interceptor) {
        // 创建 DesensitizationInterceptor 拦截器
        DesensitizationInterceptor inner = new DesensitizationInterceptor();
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

}
