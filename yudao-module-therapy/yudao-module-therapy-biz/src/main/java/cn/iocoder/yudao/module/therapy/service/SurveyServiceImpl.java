package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyQuestionMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.boot.enums.ErrorCodeConstants.SURVEY_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Resource
    private TreatmentSurveyMapper treatmentSurveyMapper;
    @Resource
    private SurveyQuestionMapper surveyQuestionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createSurvey(SurveySaveReqVO vo) {
        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        treatmentSurveyDO.setCreator(getLoginUserId() + "");
        treatmentSurveyDO.setUpdater(treatmentSurveyDO.getCreator());
        treatmentSurveyDO.setCreateTime(LocalDateTime.now());
        treatmentSurveyDO.setUpdateTime(LocalDateTime.now());
        treatmentSurveyMapper.insert(treatmentSurveyDO);
        List<QuestionDO> qst = new ArrayList<>();
        for (var item : vo.getQuestions()) {
            QuestionDO q = SurveyConvert.INSTANCE.convert(item);
            q.setBelongSurveyId(treatmentSurveyDO.getId());
            qst.add(q);
        }
        surveyQuestionMapper.insertBatch(qst);

        return treatmentSurveyDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSurvey(SurveySaveReqVO vo) {
        TreatmentSurveyDO tsDO = treatmentSurveyMapper.selectById(vo.getId());
        if (Objects.isNull(tsDO)) throw exception(SURVEY_NOT_EXISTS);
        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        treatmentSurveyDO.setUpdater(getLoginUserId() + "");
        treatmentSurveyDO.setUpdateTime(LocalDateTime.now());
        treatmentSurveyMapper.updateBatch(treatmentSurveyDO);

        //先删除再插入
        surveyQuestionMapper.deleteBySurveyId(vo.getId());
        List<QuestionDO> qst = new ArrayList<>();
        for (var item : vo.getQuestions()) {
            QuestionDO q = SurveyConvert.INSTANCE.convert(item);
            q.setBelongSurveyId(treatmentSurveyDO.getId());
            qst.add(q);
        }
        surveyQuestionMapper.insertBatch(qst);
    }

    @Override
    public PageResult<TreatmentSurveyDO> getSurveyPage(SurveyPageReqVO pageReqVO) {
        PageResult<TreatmentSurveyDO> list=treatmentSurveyMapper.selectPage(pageReqVO);
        return null;
    }
}
