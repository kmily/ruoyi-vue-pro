package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_EXISTS_UNFINISHED;
import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

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

//        treatmentSurveyDO.setCreator(getLoginUserId() + "");
//        treatmentSurveyDO.setUpdater(treatmentSurveyDO.getCreator());
//        treatmentSurveyDO.setCreateTime(LocalDateTime.now());
//        treatmentSurveyDO.setUpdateTime(LocalDateTime.now());
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
//        treatmentSurveyDO.setUpdater(getLoginUserId() + "");
//        treatmentSurveyDO.setUpdateTime(LocalDateTime.now());
        treatmentSurveyMapper.updateBatch(treatmentSurveyDO);

        //先删除再插入
        surveyQuestionMapper.deleteBySurveyId(vo.getId());
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
    public List<QuestionDO> getQuestionBySurveyId(Long id) {
        return surveyQuestionMapper.selectBySurveyId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitSurvey(SubmitSurveyReqVO reqVO) {
//        //判断问卷是否存在，状态是否正常
//        TreatmentSurveyDO tsDO = treatmentSurveyMapper.selectById(reqVO.getSurveyId());
//        if (Objects.isNull(tsDO)) throw exception(SURVEY_NOT_EXISTS);
//
//        //判断是否必答题有遗漏
//        Set<Long> qst1Set = reqVO.getQstList().stream().map(p -> p.getId()).collect(Collectors.toSet());
//        List<QuestionDO> qst = surveyQuestionMapper.selectBySurveyId(reqVO.getSurveyId());
//        Set<Long> qst2Set = qst.stream().filter(k -> k.isRequired()).map(k -> k.getId()).collect(Collectors.toSet());
//        qst2Set.removeAll(qst1Set);
//        if (qst2Set.size() > 0) {
//            throw exception(SURVEY_EXISTS_UNFINISHED);
//        }
//        //保存一次回答
//        SurveyAnswerDO answerDO = new SurveyAnswerDO();
//        answerDO.setSource(reqVO.getSource());
//        answerDO.setBelongSurveyId(tsDO.getId());
//        surveyAnswerMapper.insert(answerDO);
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
