package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.core.collection.CollectionUtil;
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
import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
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
    private SurveyStrategyFactory surveyStrategyFactory;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createSurvey(SurveySaveReqVO vo) {
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(vo.getSurveyType()).getCode());
        surveyStrategy.validationReqVO(vo);

        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        surveyStrategy.fillSurveyCode(treatmentSurveyDO);
        treatmentSurveyMapper.insert(treatmentSurveyDO);

        if (CollectionUtil.isNotEmpty(vo.getQuestions())) {
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
    public Long submitSurveyForTools(SubmitSurveyReqVO reqVO) {
        if ((Objects.isNull(reqVO.getSurveyType()) || Objects.isNull(SurveyType.getByType(reqVO.getSurveyType())))
                && (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0)) {
            throw exception(BAD_REQUEST, "id和type不能同时为空");
        }
        return 0L;
        //判断问卷是否存在，状态是否正常
//        TreatmentSurveyDO tsDO = StringUtil.isBlank(reqVO.getSurveyCode())
//                ? treatmentSurveyMapper.selectByCode(reqVO.getSurveyCode()) : treatmentSurveyMapper.selectById(reqVO.getSurveyId());
//        if (Objects.isNull(tsDO)) throw exception(SURVEY_NOT_EXISTS);
//        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(tsDO.getSurveyType()).getCode());
//
//        //判断是否必答题有遗漏
//        List<QuestionDO> qsts = surveyQuestionMapper.selectBySurveyId(reqVO.getSurveyId());
//        surveyStrategy.checkLoseQuestion(reqVO, qsts);
//        //保存一次回答
//        Long anSwerId = surveyStrategy.saveAnswer(reqVO);
//        //保存答案明细
//        reqVO.setId(anSwerId);
//        surveyStrategy.saveAnswerDetail(qsts, reqVO);
//        return anSwerId;
    }

    @Override
    public Long submitSurveyForFlow(SubmitSurveyReqVO reqVO) {
        return null;
    }

    @Override
    public PageResult<SurveyAnswerDO> getSurveyAnswerPage(SurveyAnswerPageReqVO vo) {
        return surveyAnswerMapper.selectPage(vo);
    }

    @Override
    public List<TreatmentSurveyDO> getSurveyByIds(Collection<Long> ids) {
        return treatmentSurveyMapper.selectBatchIds(ids);
    }

    @Override
    public Long initSurveyAnswer(String surveyCode, Integer source) {
        TreatmentSurveyDO ts = treatmentSurveyMapper.selectByCode(surveyCode);
        if (Objects.isNull(ts)) throw exception(SURVEY_NOT_EXISTS);
        SurveyAnswerDO answerDO = new SurveyAnswerDO();
        answerDO.setSurveyType(ts.getSurveyType());
        answerDO.setSource(source);
        answerDO.setBelongSurveyId(ts.getId());
        surveyAnswerMapper.insert(answerDO);
        return answerDO.getId();
    }
}
