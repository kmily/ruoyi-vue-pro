package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
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
 * 心情评分问卷策略实现
 */
@Component("mood_markSurveyStrategy")
public class MoodMarkSurveyStrategy extends AbstractStrategy implements SurveyStrategy {
    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;

    @Override
    public void generateReport(Long answerId) {
        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS)) {
            return;
        }
        Integer score = 0;
        for (int i = 0; i < detailDOS.size(); i++) {
            if (Objects.isNull(detailDOS.get(i).getAnswer())) {
                score += detailDOS.get(i).getAnswer().getInt("val", 0);

            }
        }

        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("score", score);
        answerDO.setReprot(jsonObject.toString());
        surveyAnswerMapper.updateById(answerDO);
    }
}
