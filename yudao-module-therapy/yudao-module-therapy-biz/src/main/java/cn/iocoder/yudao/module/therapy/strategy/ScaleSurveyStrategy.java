package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import jodd.util.StringUtil;
import org.springframework.stereotype.Component;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_EMPTY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Component("scaleSurveyStrategy")
public class ScaleSurveyStrategy implements SurveyStrategy {
    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
        if (CollUtil.isEmpty(vo.getQuestions())) throw exception(SURVEY_QUESTION_EMPTY);
    }

    @Override
    public void fillSurveyCode(TreatmentSurveyDO surveyDO) {
        surveyDO.setCode(IdUtil.fastSimpleUUID());
    }

}
