package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.config.ScaleReportAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GAD7量表问卷策略实现
 */
@Component("gad7_scaleSurveyStrategy")
public class GAD7ScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {

    @Autowired
    private ScaleReportAutoConfiguration scaleReportAutoConfiguration;

    @Override
    public void generateReport(Long details) {
        super.generateScaleReport(details,scaleReportAutoConfiguration.getGad7());
    }
}
