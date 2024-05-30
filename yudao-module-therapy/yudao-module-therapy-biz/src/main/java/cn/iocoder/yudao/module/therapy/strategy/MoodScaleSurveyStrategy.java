package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import org.springframework.stereotype.Component;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_QUANTITY_INACCURACY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 积极情绪和消极情绪量表问卷策略实现
 */
@Component("mood_scaleSurveyStrategy")
public class MoodScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {

    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
        super.validationReqVO(vo);
        if (vo.getQuestions().size() != 20) {
            throw exception(SURVEY_QUESTION_QUANTITY_INACCURACY, "积极情绪和消极情绪量表必须是20道题");
        }
    }
}
