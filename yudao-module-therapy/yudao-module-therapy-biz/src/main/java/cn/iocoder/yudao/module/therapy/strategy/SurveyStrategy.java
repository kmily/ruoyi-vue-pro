package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;

import java.util.List;

public interface SurveyStrategy {
    /**
     * 验证创建/更新参数
     * @param vo
     */
    default void validationReqVO(SurveySaveReqVO vo)
    {
        return;
    }

    /**
     * 填充题目code
     */
    default void fillSurveyCode(TreatmentSurveyDO surveyDO){

    }

    /**
     * 填充问题code
     * @param qst
     */
    default void fillQuestionCode(QuestionDO qst){
//        if (StringUtil.isBlank(qst.getCode())) qst.setCode(IdUtil.fastSimpleUUID());
    }

    /**
     * 检查是否有必答题没做
     * @param reqVO
     * @param qst
     */
    void checkLoseQuestion(SubmitSurveyReqVO reqVO, List<QuestionDO> qst);

    /**
     * 保存一次回答
     * @param reqVO
     * @return
     */
    Long saveAnswer(SubmitSurveyReqVO reqVO);

    /**
     * 保存回答明细
     * @param qst
     * @param reqVO
     */
    void saveAnswerDetail(List<QuestionDO> qst,SubmitSurveyReqVO reqVO);

    default JSONObject getSurveyReport(Long answerId){
        return new JSONObject();
    }
}
