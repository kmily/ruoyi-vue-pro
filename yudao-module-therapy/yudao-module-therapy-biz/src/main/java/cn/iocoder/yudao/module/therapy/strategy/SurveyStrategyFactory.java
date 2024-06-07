package cn.iocoder.yudao.module.therapy.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SurveyStrategyFactory {
    private final ApplicationContext applicationContext;

    @Autowired
    public SurveyStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public SurveyStrategy getSurveyStrategy(String type) {
        if(applicationContext.containsBean(this.getName(type))){
            return applicationContext.getBean(this.getName(type), SurveyStrategy.class);
        }else {
            return applicationContext.getBean(DefaultSurveyStrategy.class);
        }
//        SurveyStrategy surveyStrategy = applicationContext.getBean(this.getName(type), SurveyStrategy.class);
//        if (surveyStrategy != null) {
//            return surveyStrategy;
//        }


    }

    private String getName(String type) {
        return String.format("%sSurveyStrategy", type);
    }
}
