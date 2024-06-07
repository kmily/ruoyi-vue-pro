package cn.iocoder.yudao.module.therapy;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;

public class MyFlowableEngineConfigure implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        // advanced configuration
    }

}
