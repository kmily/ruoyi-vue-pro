package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.ReprotState;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 心情日记策略实现
 */
@Component("mood_diarySurveyStrategy")
public class MoodDiarySurveyStrategy  implements SurveyStrategy {
    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;
    @Resource
    private SurveyAnswerMapper surveyAnswerMapper;
    @Override
    public void generateReport(Long answerId) {
        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS)) {
            return;
        }
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        answerDO.setReprot(detailDOS.get(0).getAnswer().toString());//按照约定将答案做为报告,以便后续出列表使用
        answerDO.setReprotState(ReprotState.DONE.getType());
        surveyAnswerMapper.updateById(answerDO);
    }
}
