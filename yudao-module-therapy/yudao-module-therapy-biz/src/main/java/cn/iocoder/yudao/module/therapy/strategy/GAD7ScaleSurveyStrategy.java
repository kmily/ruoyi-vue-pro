package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.config.ScaleReportAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GAD7量表问卷策略实现
 */
@Component("gad7_scaleSurveyStrategy")
public class GAD7ScaleSurveyStrategy extends AbstractStrategy implements SurveyStrategy {

    @Autowired
    private ScaleReportAutoConfiguration scaleReportAutoConfiguration;

    @Override
    public void generateReport(Long answerId) {
//        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
//        if (CollectionUtil.isEmpty(detailDOS)) {
//            return;
//        }
//        Integer score = 0;
//        for (AnswerDetailDO item : detailDOS) {
//            if (Objects.isNull(item.getAnswer())) {
//                score += item.getAnswer().getInt("val", 0);
//            }
//        }
//        List<ScaleReportAutoConfiguration.Grade> grades=scaleReportAutoConfiguration.getGad7();
//        Integer finalScore = score;
//        ScaleReportAutoConfiguration.Grade grade=grades.stream().filter(p->p.getBegin()<= finalScore && p.getEnd()>= finalScore).findFirst().get();
//        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.set("score",score);
//        jsonObject.set("explain",grade.getExplain());
//        jsonObject.set("shortExplain",grade.getShortExplain());
//        jsonObject.set("begin",grade.getBegin());
//        jsonObject.set("end",grade.getEnd());
//        answerDO.setReprot(jsonObject.toString());
//        surveyAnswerMapper.updateById(answerDO);
        super.generateReport(answerId,scaleReportAutoConfiguration.getGad7());
    }
}
