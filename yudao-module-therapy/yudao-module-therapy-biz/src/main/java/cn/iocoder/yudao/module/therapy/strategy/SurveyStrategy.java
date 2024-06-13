package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;

import java.util.List;

public interface SurveyStrategy {
    /**
     * 验证创建/更新参数
     *
     * @param vo
     */
    default void validationReqVO(SurveySaveReqVO vo) {
        return;
    }

    /**
     * 填充题目code
     */
    default void fillSurveyCode(TreatmentSurveyDO surveyDO) {

    }

    /**
     * 填充问题code
     *
     * @param qst
     */
    default void fillQuestionCode(QuestionDO qst) {
//        if (StringUtil.isBlank(qst.getCode())) qst.setCode(IdUtil.fastSimpleUUID());
    }

    /**
     * 检查是否有必答题没做
     *
     * @param reqVO
     * @param qst
     */
    void checkLoseQuestion(SubmitSurveyReqVO reqVO, List<QuestionDO> qst);

    /**
     * 检查题目是否属于问卷
     * @param reqVO
     * @param qst
     */
    void checkQuestionExistsSurvey(SubmitSurveyReqVO reqVO, List<QuestionDO> qst);
    /**
     * 获取报告
     *
     * @param answerId
     * @return
     */
    default JSONObject getSurveyReport(Long answerId) {
        return new JSONObject();
    }

    /**
     * 补充问题列表
     *
     * @param vo
     */
    default void fillQuestion(SurveySaveReqVO vo) {
    }

    /**
     * 生成报告
     */
    default void generateReport(Long details){}
}
