package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动化思维识别策略实现
 */
@Component("auto_thought_recognitionSurveyStrategy")
public class AutoThoughtRecognitionSurveyStrategy extends AbstractStrategy implements SurveyStrategy {
    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
        SurveyStrategy.super.validationReqVO(vo);
    }

    @Override
    public void fillSurveyCode(TreatmentSurveyDO surveyDO) {
        SurveyStrategy.super.fillSurveyCode(surveyDO);
    }

    @Override
    public void fillQuestionCode(QuestionDO qst) {
        SurveyStrategy.super.fillQuestionCode(qst);
    }

    @Override
    public void checkLoseQuestion(SubmitSurveyReqVO reqVO, List<QuestionDO> qst) {
        SurveyStrategy.super.checkLoseQuestion(reqVO,qst);
    }
    @Override
    public void checkQuestionExistsSurvey(SubmitSurveyReqVO reqVO, List<QuestionDO> qst){
        SurveyStrategy.super.checkQuestionExistsSurvey(reqVO,qst);
    }

    @Override
    public void generateReport(Long answerId) {
        super.generateReport(answerId);
    }
}
