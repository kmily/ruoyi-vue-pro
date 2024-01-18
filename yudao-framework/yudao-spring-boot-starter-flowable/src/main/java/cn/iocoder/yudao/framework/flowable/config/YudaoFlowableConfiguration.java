package cn.iocoder.yudao.framework.flowable.config;

import cn.iocoder.yudao.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.yudao.framework.flowable.core.web.FlowableWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@AutoConfiguration
public class YudaoFlowableConfiguration {

    /**
     * 参考 {@link org.flowable.spring.boot.FlowableJobConfiguration} 类，创建对应的 AsyncTaskExecutor Bean
     *
     * 如果不创建，会导致项目启动时，Flowable 报错的问题
     */
    @Bean
    @Primary
    public org.springframework.core.task.AsyncTaskExecutor flowableAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("flowable-task-Executor-");
        executor.setAwaitTerminationSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    /**
     * 配置 flowable Web 过滤器
     */
    @Bean
    public FilterRegistrationBean<FlowableWebFilter> flowableWebFilter() {
        FilterRegistrationBean<FlowableWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new FlowableWebFilter());
        registrationBean.setOrder(WebFilterOrderEnum.FLOWABLE_FILTER);
        return registrationBean;
    }

}
