package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
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

    /**
     * 删除问卷
     * @param id
     */
    void del(Long id);

    /**
     * 通过问卷获取所题目
     * @param id
     * @return
     */
    List<QuestionDO> getQuestionBySurveyId(Long id);

    /**
     * 工具箱提交答案
     * @param reqVO
     * @return
     */
    Long submitSurveyForTools(SubmitSurveyReqVO reqVO);

    /**
     * 流程提交答案
     * @param reqVO
     * @return
     */
    Long submitSurveyForFlow(SubmitSurveyReqVO reqVO);

    /**
     * 分页获取答题记录
     * @param vo
     * @return
     */
    PageResult<SurveyAnswerDO> getSurveyAnswerPage(SurveyAnswerPageReqVO vo);

    /**
     * 批量获取问卷
     * @param ids
     * @return
     */
    List<TreatmentSurveyDO> getSurveyByIds(Collection<Long> ids);

    /**
     * 初始化一次回答实例
     * @param surveyCode
     * @param source
     * @return
     */
    Long initSurveyAnswer(String surveyCode,Integer source);

    /**
     * 获取一次答题的明细
     * @param answerId
     * @return
     */
    List<AnswerDetailDO> getAnswerDetailByAnswerId(Long answerId);

    Long getSurveyIdByCode(String surveyCode);

    SurveyAnswerDO getAnswerDO(Long id);

    /**
     * 获取患者最新目标与动机
     * @return
     */
    SubmitSurveyReqVO getGoalMotive(Long userId);

    /**
     * 获取最新思维陷井
     * @return
     */
    SubmitSurveyReqVO getThoughtTrap();
}
