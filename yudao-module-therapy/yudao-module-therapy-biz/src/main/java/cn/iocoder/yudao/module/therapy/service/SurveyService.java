package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;

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
}
