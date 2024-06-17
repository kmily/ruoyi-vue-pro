package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.config.ScaleReportAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ISI量表问卷策略实现
 */
@Component("isi_scaleSurveyStrategy")
public class ISIScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {
    @Autowired
    private ScaleReportAutoConfiguration scaleReportAutoConfiguration;

    @Override
    public void generateReport(Long details) {
        super.generateScaleReport(details,scaleReportAutoConfiguration.getIsi());
    }
}
