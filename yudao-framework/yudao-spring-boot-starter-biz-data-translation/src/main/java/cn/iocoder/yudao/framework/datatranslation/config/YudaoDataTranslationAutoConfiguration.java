package cn.iocoder.yudao.framework.datatranslation.config;

import cn.iocoder.yudao.framework.datatranslation.core.aop.DataTranslationAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class YudaoDataTranslationAutoConfiguration {

    @Bean
    public DataTranslationAspect dataTranslationAspect() {
        return new DataTranslationAspect();
    }

}
