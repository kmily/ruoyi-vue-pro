package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.json.JSONObject;
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
}
