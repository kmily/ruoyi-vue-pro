package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认知重建策略实现
 */
@Component("cognize_rebuildSurveyStrategy")
public class CognizeRebuildSurveyStrategy  implements SurveyStrategy {

    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;
    @Resource
    private SurveyAnswerMapper surveyAnswerMapper;
    @Override
    public void generateReport(Long answerId) {
        //获取关联自动化思维id
        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS)) {
            return;
        }
        Long autoThoughtId = detailDOS.get(0).getAnswer().getLong("autoThoughtId", 0L);
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(autoThoughtId);
        List<AnswerDetailDO> detailDOS2 = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS2)) {
            return;
        }
        //拼接认知重建
        JSONObject jsonObject = new JSONObject(answerDO.getReprot());
        jsonObject.putAll(detailDOS2.get(0).getAnswer());

        SurveyAnswerDO answerDO2 = surveyAnswerMapper.selectById(answerId);
        answerDO2.setReprot(jsonObject.toString());
        surveyAnswerMapper.updateById(answerDO2);
    }
}
