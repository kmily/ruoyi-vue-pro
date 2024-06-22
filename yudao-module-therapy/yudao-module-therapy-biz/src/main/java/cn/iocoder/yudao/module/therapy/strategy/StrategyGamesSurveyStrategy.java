package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_EMPTY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 对策游戏问卷策略实现
 */
@Component("strategy_gamesSurveyStrategy")
public class StrategyGamesSurveyStrategy implements SurveyStrategy {
    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
        if (CollUtil.isEmpty(vo.getQuestions())) throw exception(SURVEY_QUESTION_EMPTY);
    }
}
