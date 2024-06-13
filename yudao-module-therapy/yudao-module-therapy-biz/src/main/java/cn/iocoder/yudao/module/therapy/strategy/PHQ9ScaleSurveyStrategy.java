package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.config.ScaleReportAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PHQ9量表问卷策略实现
 */
@Component("phq9_scaleSurveyStrategy")
public class PHQ9ScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {

    @Override
    public JSONObject getSurveyReport(Long answerId) {
        return SurveyStrategy.super.getSurveyReport(answerId);
    }

    @Autowired
    private ScaleReportAutoConfiguration scaleReportAutoConfiguration;

    @Override
    public void generateReport(Long answerId) {
        super.generateReport(answerId,scaleReportAutoConfiguration.getPhq9());
    }
}
