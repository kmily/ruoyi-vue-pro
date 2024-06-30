package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.ReprotState;
import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
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
    private SurveyStrategyFactory surveyStrategyFactory;

    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createSurvey(SurveySaveReqVO vo) {
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(vo.getSurveyType()).getCode());
        surveyStrategy.validationReqVO(vo);

        TreatmentSurveyDO treatmentSurveyDO = SurveyConvert.INSTANCE.convert(vo);
        surveyStrategy.fillSurveyCode(treatmentSurveyDO);
        treatmentSurveyMapper.insert(treatmentSurveyDO);

        surveyStrategy.fillQuestion(vo);//填充题目,量表除外
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
        treatmentSurveyDO.setCode(tsDO.getCode());//不能改变原来code,可能后面已经使用了
        treatmentSurveyMapper.updateById(treatmentSurveyDO);

        //先删除再插入
        surveyQuestionMapper.deleteAbsoluteBySurveyId(vo.getId());
        surveyStrategy.fillQuestion(vo);
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

    //@Transactional(rollbackFor = Exception.class)
    @Override
    public Long submitSurveyForTools(SubmitSurveyReqVO reqVO) {
        if (CollectionUtil.isEmpty(reqVO.getQstList())) {
            throw exception(BAD_REQUEST, "提交的答案是空的");
        }
        if ((Objects.isNull(reqVO.getSurveyType()) || Objects.isNull(SurveyType.getByType(reqVO.getSurveyType())))
                && (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0)) {
            throw exception(BAD_REQUEST, "id和type不能同时为空");
        }

        if (Objects.nonNull(reqVO.getId()) && reqVO.getId() > 0) {//编辑原有
            updateOrAddAnswer(reqVO);
            return reqVO.getId();
        } else {//通过类型创建答题
            //获取问卷通过类型
            TreatmentSurveyDO tsdo = treatmentSurveyMapper.selectFirstByType(reqVO.getSurveyType());
            if (Objects.isNull(tsdo)) {
                throw exception(SURVEY_NOT_EXISTS);
            }
            //检查题目是否属于问卷
            List<QuestionDO> ts = surveyQuestionMapper.selectBySurveyId(tsdo.getId());
            SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(reqVO.getSurveyType()).getCode());
            surveyStrategy.checkQuestionExistsSurvey(reqVO, ts);
            Long answerId = this.initSurveyAnswer(tsdo.getCode(), 1);
            Map<String, QuestionDO> mapQst = CollectionUtils.convertMap(ts, QuestionDO::getCode);
            List<AnswerDetailDO> newDetails = new ArrayList<>();
            for (AnAnswerReqVO item : reqVO.getQstList()) {
                AnswerDetailDO detailDO = new AnswerDetailDO();
                QuestionDO qst = mapQst.getOrDefault(item.getQstCode(), null);
                detailDO.setAnswerId(answerId);
                detailDO.setBelongSurveyId(tsdo.getId());
                detailDO.setCreator(StringUtils.isBlank(getLoginUserId().toString())? "system":getLoginUserId().toString());
                detailDO.setUpdater(StringUtils.isBlank(getLoginUserId().toString())? "system":getLoginUserId().toString());
                detailDO.setQstContext(Objects.isNull(qst) ? null : qst.getQstContext());
                detailDO.setAnswer(item.getAnswer());
                detailDO.setQstId(Objects.isNull(qst) ? 0L : qst.getId());
                detailDO.setBelongQstCode(item.getQstCode());
                newDetails.add(detailDO);
            }
            surveyAnswerDetailMapper.insertBatch(newDetails);
            surveyStrategy.generateReport(answerId);
            surveyAnswerMapper.updateReprotState(answerId,ReprotState.DONE.getType());
            return answerId;
        }
    }

    private void updateOrAddAnswer(SubmitSurveyReqVO reqVO) {
        //检查之前的答题是不是存在
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(reqVO.getId());
        if (Objects.isNull(answerDO)) {
            throw exception(SURVEY_ANSWER_NOT_EXISTS);
        }
        SurveyStrategy surveyStrategy = surveyStrategyFactory.getSurveyStrategy(SurveyType.getByType(answerDO.getSurveyType()).getCode());

        //检查题目是否属于问卷
        List<QuestionDO> ts = surveyQuestionMapper.selectBySurveyId(answerDO.getBelongSurveyId());
        surveyStrategy.checkQuestionExistsSurvey(reqVO, ts);

        //确定是更新还是新插入
        Map<String, QuestionDO> mapQst = CollectionUtils.convertMap(ts, QuestionDO::getCode);
        List<AnswerDetailDO> details = surveyAnswerDetailMapper.getByAnswerId(reqVO.getId());
        Map<String, AnswerDetailDO> map = CollectionUtils.convertMap(details, AnswerDetailDO::getBelongQstCode);
        List<AnswerDetailDO> newDetails = new ArrayList<>();
        List<AnswerDetailDO> oldDetails = new ArrayList<>();
        for (AnAnswerReqVO item : reqVO.getQstList()) {
            if (map.containsKey(item.getQstCode())) {
                AnswerDetailDO temp = map.get(item.getQstCode());
                temp.setAnswer(item.getAnswer());
                oldDetails.add(temp);
            } else {
                AnswerDetailDO detailDO = new AnswerDetailDO();
                QuestionDO qst = mapQst.getOrDefault(item.getQstCode(), null);;
                detailDO.setAnswerId(reqVO.getId());
                detailDO.setBelongSurveyId(answerDO.getBelongSurveyId());
                detailDO.setQstContext(Objects.isNull(qst) ? null : qst.getQstContext());
                detailDO.setAnswer(item.getAnswer());
                detailDO.setQstId(Objects.isNull(qst) ? null : qst.getId());
                detailDO.setBelongQstCode(item.getQstCode());
                detailDO.setQstType(Objects.isNull(qst) ? SurveyQuestionType.SPACES.getType() : qst.getQstType());
                newDetails.add(detailDO);
            }
        }
        if (CollectionUtil.isNotEmpty(oldDetails)) {
            surveyAnswerDetailMapper.updateBatch(oldDetails);
        }
        if (CollectionUtil.isNotEmpty(newDetails)) {
            surveyAnswerDetailMapper.insertBatch(newDetails);
        }

        surveyStrategy.generateReport(answerDO.getId());
        surveyAnswerMapper.updateReprotState(answerDO.getId(),ReprotState.DONE.getType());
    }

   // @Transactional(rollbackFor = Exception.class)
    @Override
    public Long submitSurveyForFlow(SubmitSurveyReqVO reqVO) {
        if (CollectionUtil.isEmpty(reqVO.getQstList())) {
            throw exception(BAD_REQUEST, "提交的答案是空的");
        }
        if (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0) {
            throw exception(BAD_REQUEST, "id不能为空");
        }
        updateOrAddAnswer(reqVO);

        return reqVO.getId();
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

    @Override
    public List<AnswerDetailDO> getAnswerDetailByAnswerId(Long answerId) {
        return surveyAnswerDetailMapper.getByAnswerId(answerId);
    }

    public Long getSurveyIdByCode(String surveyCode) {
        TreatmentSurveyDO treatmentSurveyDO = treatmentSurveyMapper.selectByCode(surveyCode);
        return treatmentSurveyDO.getId();
    }

    @Override
    public SurveyAnswerDO getAnswerDO(Long id) {
        return surveyAnswerMapper.selectById(id);
    }

    @Override
    public SubmitSurveyReqVO getGoalMotive(Long userId) {
        return this.getFirstAnswerInfo(SurveyType.PROBLEM_GOAL_MOTIVE.getType(),userId);
    }

    private SubmitSurveyReqVO getFirstAnswerInfo(Integer type,Long userId) {
        List<SurveyAnswerDO> surveyAnswerDOS = surveyAnswerMapper.selectBySurveyTypeAndUserId(userId, Arrays.asList(type));
        Optional<SurveyAnswerDO> optional = surveyAnswerDOS.stream().max(Comparator.comparingLong(SurveyAnswerDO::getId));
        if (!optional.isPresent()) {
            return null;
        }
        List<AnswerDetailDO> answerDetailDOS = surveyAnswerDetailMapper.getByAnswerId(optional.get().getId());
        if (CollectionUtil.isEmpty(answerDetailDOS)) {
            return null;
        }
        SubmitSurveyReqVO vo = new SubmitSurveyReqVO();
        vo.setId(optional.get().getId());
        vo.setSurveyType(optional.get().getSurveyType());
        vo.setQstList(new ArrayList<>());
        for (AnswerDetailDO item : answerDetailDOS) {
            AnAnswerReqVO reqVO = new AnAnswerReqVO();
            reqVO.setQstCode(item.getBelongQstCode());
            reqVO.setAnswer(item.getAnswer());
            vo.getQstList().add(reqVO);
        }
        return vo;
    }


    @Override
    public SubmitSurveyReqVO getThoughtTrap() {
        return this.getFirstAnswerInfo(SurveyType.TWELVE_MIND_DISTORT.getType(),getLoginUserId());
    }

    @Override
    public SubmitSurveyReqVO getMoodRecognition() {
        return this.getFirstAnswerInfo(SurveyType.MOOD_RECOGNITION.getType(),getLoginUserId());
    }

    @Override
    public SubmitSurveyReqVO getHappyActivity() {
        return this.getFirstAnswerInfo(SurveyType.HAPPY_ACTIVITY.getType(),getLoginUserId());
    }

    @Override
    public void setSurveyRel(Long id, Long relId) {
        TreatmentSurveyDO surveyDO = treatmentSurveyMapper.selectById(id);
        if (Objects.isNull(surveyDO)) {
            throw exception(SURVEY_NOT_EXISTS);
        }
        TreatmentSurveyDO surveyDO1 = treatmentSurveyMapper.selectById(relId);
        if (Objects.isNull(surveyDO1)) {
            throw exception(SURVEY_NOT_EXISTS);
        }

        surveyDO.setRelSurveyList(Arrays.asList(relId));
        treatmentSurveyMapper.updateById(surveyDO);
    }

    @Override
    public List<TreatmentSurveyDO> listByType(Integer type) {
        return treatmentSurveyMapper.listByType(type);
    }

    @Override
    public List<TreatmentSurveyDO> listByTag(String tag) {
        if(StringUtils.isBlank(tag)){
            throw exception(BAD_REQUEST);
        }
        return treatmentSurveyMapper.listByTag(tag);
    }
}
