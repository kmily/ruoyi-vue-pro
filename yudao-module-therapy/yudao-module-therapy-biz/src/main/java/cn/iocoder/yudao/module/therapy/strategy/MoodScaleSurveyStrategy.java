package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.ReprotState;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_QUANTITY_INACCURACY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 积极情绪和消极情绪量表问卷策略实现
 */
@Component("mood_scaleSurveyStrategy")
public class MoodScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {
    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;

    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
        super.validationReqVO(vo);
        if (vo.getQuestions().size() != 20) {
            throw exception(SURVEY_QUESTION_QUANTITY_INACCURACY, "积极情绪和消极情绪量表必须是20道题");
        }
    }

    @Override
    public void generateReport(Long answerId) {
        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS)) {
            return;
        }
        Integer positiveScore = 0;//积极
        Integer passiveScore = 0;//消极
        List<Integer> indexs = Arrays.asList(0, 4, 5, 8, 9, 11, 15, 16, 18, 19);
        for (int i = 0; i < detailDOS.size(); i++) {
            if (Objects.nonNull(detailDOS.get(i).getAnswer())) {
                if (indexs.contains(i)) {
                    positiveScore += detailDOS.get(i).getAnswer().getInt("val", 0);
                } else {
                    passiveScore += detailDOS.get(i).getAnswer().getInt("val", 0);
                }
            }
        }

        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("positiveScore", positiveScore);
        jsonObject.set("passiveScore", passiveScore);
        answerDO.setReprot(jsonObject.toString());
        surveyAnswerMapper.updateById(answerDO);
    }
}
