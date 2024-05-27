package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import jodd.util.StringUtil;

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
    void fillSurveyCode(TreatmentSurveyDO surveyDO);

    /**
     * 填充问题code
     * @param qst
     */
    default void fillQuestionCode(QuestionDO qst){
        if (StringUtil.isBlank(qst.getCode())) qst.setCode(IdUtil.fastSimpleUUID());
    }
}
