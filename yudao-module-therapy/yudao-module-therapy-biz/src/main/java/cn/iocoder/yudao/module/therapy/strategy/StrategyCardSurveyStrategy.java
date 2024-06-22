package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 对策卡问卷策略实现
 */
@Component("strategy_cardSurveyStrategy")
public class StrategyCardSurveyStrategy  implements SurveyStrategy {
    @Resource
    private TreatmentSurveyMapper treatmentSurveyMapper;

    @Resource
    private SurveyAnswerMapper surveyAnswerMapper;

    @Override
    public void generateReport(Long answerId) {
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        if (Objects.isNull(answerDO)) {
            return;
        }
        TreatmentSurveyDO surveyDO = treatmentSurveyMapper.selectById(answerDO.getBelongSurveyId());
        if (Objects.isNull(surveyDO)) {
            return;
        }
        //问卷的tag和title做为报告，供后面构建对策卡页分类和标题用
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("category", surveyDO.getTags().get(0));//对策卡问卷按约定只录入一个tag
        jsonObject.set("title", surveyDO.getTitle());
        answerDO.setReprot(jsonObject.toString());
        surveyAnswerMapper.updateById(answerDO);
    }
}
