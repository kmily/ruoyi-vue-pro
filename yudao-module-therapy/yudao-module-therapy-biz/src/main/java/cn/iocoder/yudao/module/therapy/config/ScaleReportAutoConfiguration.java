package cn.iocoder.yudao.module.therapy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "scale")
@PropertySource(value ="classpath:custom.yaml",encoding = "UTF-8",ignoreResourceNotFound = false,factory = YAMLPropertySourceFactory.class)
public class ScaleReportAutoConfiguration {
    private List<Grade> gad7;
    private List<Grade> phq9;
    private List<Grade> isi;

    @Data
    public static class Grade{
        private Integer begin;
        private Integer end;
        private String shortExplain;
        private String explain;
    }
}
