package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应对卡策略实现
 */
@Component("reply_cardSurveyStrategy")
public class ReplyCardSurveyStrategy implements SurveyStrategy {
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
        surveyAnswerMapper.updateById(answerDO);
    }
}
