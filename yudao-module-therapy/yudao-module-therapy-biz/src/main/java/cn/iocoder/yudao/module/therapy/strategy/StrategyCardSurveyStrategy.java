package cn.iocoder.yudao.module.therapy.strategy;

import org.springframework.stereotype.Component;

/**
 * 对策卡问卷策略实现
 */
@Component("strategy_cardSurveyStrategy")
public class StrategyCardSurveyStrategy extends AbstractStrategy implements SurveyStrategy {


    @Override
    public void generateReport(Long details) {
        SurveyStrategy.super.generateReport(details);
    }
}
