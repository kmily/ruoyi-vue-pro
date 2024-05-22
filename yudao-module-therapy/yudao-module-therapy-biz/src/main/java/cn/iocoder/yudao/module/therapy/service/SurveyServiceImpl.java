package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.springframework.stereotype.Service;

@Service
public class SurveyServiceImpl implements SurveyService{
    @Override
    public Long createSurvey(SurveySaveReqVO vo) {
        return null;
    }

    @Override
    public void updateSurvey(SurveySaveReqVO vo) {

    }

    @Override
    public PageResult<TreatmentSurveyDO> getSurveyPage(SurveyPageReqVO pageReqVO) {
        return null;
    }
}
