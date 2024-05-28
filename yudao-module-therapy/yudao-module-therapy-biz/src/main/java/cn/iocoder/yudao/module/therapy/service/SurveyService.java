package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;

import java.util.Collection;
import java.util.List;

public interface SurveyService {
    /**
     * 创建问卷
     * @param vo
     * @return
     */
    Long createSurvey(SurveySaveReqVO vo);

    /**
     * 更新问卷
     * @param vo
     */
    void updateSurvey(SurveySaveReqVO vo);

    /**
     * 获取问卷分页
     * @param pageReqVO
     * @return
     */
    PageResult<TreatmentSurveyDO> getSurveyPage(SurveyPageReqVO pageReqVO);

    /**
     * 获取问卷
     * @param id
     * @return
     */
    TreatmentSurveyDO get(Long id);
    void del(Long id);

    /**
     * 通过问卷获取
     * @param id
     * @return
     */
    List<QuestionDO> getQuestionBySurveyId(Long id);

    void submitSurvey(SubmitSurveyReqVO reqVO);

    PageResult<SurveyAnswerDO> getSurveyAnswerPage(SurveyAnswerPageReqVO vo);

    List<TreatmentSurveyDO> getSurveyByIds(Collection<Long> ids);
}
