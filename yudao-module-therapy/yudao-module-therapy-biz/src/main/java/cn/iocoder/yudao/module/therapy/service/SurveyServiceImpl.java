package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyQuestionMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import cn.iocoder.yudao.module.therapy.strategy.SurveyStrategy;
import cn.iocoder.yudao.module.therapy.strategy.SurveyStrategyFactory;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Resource
    private TreatmentSurveyMapper treatmentSurveyMapper;
    @Resource
    private SurveyQuestionMapper surveyQuestionMapper;
    @Resource
    private SurveyAnswerMapper surveyAnswerMapper;
    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;
    @Resource
    private SurveyStrategyFactory surveyStrategyFactory;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createSurvey(SurveySaveReqVO vo) {
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(vo.getSurveyType()).getCode());
        surveyStrategy.validationReqVO(vo);

        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        surveyStrategy.fillSurveyCode(treatmentSurveyDO);
        treatmentSurveyMapper.insert(treatmentSurveyDO);

        List<QuestionDO> qst = new ArrayList<>();
        for (var item : vo.getQuestions()) {
            QuestionDO q = SurveyConvert.INSTANCE.convertQst(new JSONObject(item));
            surveyStrategy.fillQuestionCode(q);
            q.setBelongSurveyId(treatmentSurveyDO.getId());
            q.setBelongSurveyCode(treatmentSurveyDO.getCode());
            qst.add(q);
        }
        surveyQuestionMapper.insertBatch(qst);

        return treatmentSurveyDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSurvey(SurveySaveReqVO vo) {
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(vo.getSurveyType()).getCode());
        surveyStrategy.validationReqVO(vo);

        TreatmentSurveyDO tsDO = treatmentSurveyMapper.selectById(vo.getId());
        if (Objects.isNull(tsDO)) throw exception(SURVEY_NOT_EXISTS);
        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        treatmentSurveyDO.setCode(tsDO.getCode());
        treatmentSurveyMapper.updateById(treatmentSurveyDO);

        //先删除再插入
        surveyQuestionMapper.deleteAbsoluteBySurveyId(vo.getId());
        List<QuestionDO> qst = new ArrayList<>();
        for (var item : vo.getQuestions()) {
            QuestionDO q = SurveyConvert.INSTANCE.convertQst(new JSONObject(item));
            surveyStrategy.fillQuestionCode(q);
            q.setBelongSurveyId(treatmentSurveyDO.getId());
            q.setBelongSurveyCode(treatmentSurveyDO.getCode());
            qst.add(q);
        }
        surveyQuestionMapper.insertBatch(qst);
    }

    @Override
    public PageResult<TreatmentSurveyDO> getSurveyPage(SurveyPageReqVO pageReqVO) {
        return treatmentSurveyMapper.selectPage(pageReqVO);
    }

    @Override
    public TreatmentSurveyDO get(Long id) {
        return treatmentSurveyMapper.selectById(id);
    }

    @Override
    public void del(Long id) {
        treatmentSurveyMapper.deleteById(id);
        surveyQuestionMapper.deleteBySurveyId(id);
    }

    @Override
    public List<QuestionDO> getQuestionBySurveyId(Long id) {
        return surveyQuestionMapper.selectBySurveyId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitSurvey(SubmitSurveyReqVO reqVO) {
        //判断问卷是否存在，状态是否正常
        TreatmentSurveyDO tsDO = treatmentSurveyMapper.selectById(reqVO.getSurveyId());
        if (Objects.isNull(tsDO)) throw exception(SURVEY_NOT_EXISTS);
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(tsDO.getSurveyType()).getCode());

        //判断是否必答题有遗漏
        List<QuestionDO> qsts = surveyQuestionMapper.selectBySurveyId(reqVO.getSurveyId());
        surveyStrategy.checkLoseQuestion(reqVO, qsts);
//        //保存一次回答
        surveyStrategy.saveAnswer(reqVO.getSource(), reqVO.getSurveyId());
//        //保存答案明细
//        Map<Long, QuestionDO> qstMap = CollectionUtils.convertMap(qst, QuestionDO::getId);
//        List<AnswerDetailDO> anAnswerDOS = SurveyConvert.INSTANCE.convert(qstMap, reqVO.getQstList(), getLoginUserId());
//        surveyAnswerDetailMapper.insertBatch(anAnswerDOS);
    }

    @Override
    public PageResult<SurveyAnswerDO> getSurveyAnswerPage(SurveyAnswerPageReqVO vo) {
        return surveyAnswerMapper.selectPage(vo);
    }

    @Override
    public List<TreatmentSurveyDO> getSurveyByIds(Collection<Long> ids) {
        return treatmentSurveyMapper.selectBatchIds(ids);
    }
}
