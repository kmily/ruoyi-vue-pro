package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.therapy.config.ScaleReportAutoConfiguration;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerDetailMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import jodd.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 抽象问卷策略实现
 */
public abstract class AbstractStrategy {
    @Resource
    protected SurveyAnswerMapper surveyAnswerMapper;
    @Resource
    private SurveyAnswerDetailMapper surveyAnswerDetailMapper;

    public void validationReqVO(SurveySaveReqVO vo) {
        if (CollUtil.isEmpty(vo.getQuestions())) throw exception(SURVEY_QUESTION_EMPTY);
    }

    public void fillSurveyCode(TreatmentSurveyDO surveyDO) {
        surveyDO.setCode(IdUtil.fastSimpleUUID());
    }

    /**
     * 填充问题code
     *
     * @param qst
     */
    public void fillQuestionCode(QuestionDO qst) {
        if (StringUtil.isBlank(qst.getCode())) qst.setCode(IdUtil.fastSimpleUUID());
    }

    /**
     * 检查是否有必答题没做
     *
     * @param reqVO
     * @param qst
     */
    public void checkLoseQuestion(SubmitSurveyReqVO reqVO, List<QuestionDO> qst) {
        //必须一次全部提交
        Set<String> qst1Set = reqVO.getQstList().stream().map(p -> p.getQstCode()).collect(Collectors.toSet());
        Set<Long> qst2Set = qst.stream().filter(k -> k.isRequired()).map(k -> k.getId()).collect(Collectors.toSet());
        qst2Set.removeAll(qst1Set);
        if (qst2Set.size() > 0) {
            throw exception(SURVEY_EXISTS_UNFINISHED);
        }
    }

//    public Long saveAnswer(SubmitSurveyReqVO reqVO) {
//        if (reqVO.getId() > 0) {
//            return reqVO.getId();
//        }
//        SurveyAnswerDO answerDO = new SurveyAnswerDO();
////        answerDO.setSource(reqVO.getSource());
////        answerDO.setBelongSurveyId(reqVO.getSurveyId());
//        surveyAnswerMapper.insert(answerDO);
//        return answerDO.getId();
//    }
//
//    public void saveAnswerDetail(List<QuestionDO> qst, SubmitSurveyReqVO reqVO) {
//        Map<String, QuestionDO> qstMap = CollectionUtils.convertMap(qst, QuestionDO::getCode);
//        List<AnswerDetailDO> anAnswerDOS = SurveyConvert.INSTANCE.convert(qstMap, reqVO);
//        surveyAnswerDetailMapper.insertBatch(anAnswerDOS);
//    }

//    public void fillQuestion(SurveySaveReqVO vo){
//
//    }
    /**
     * 检查题目是否属于问卷
     * @param reqVO
     * @param qst
     */
    public void checkQuestionExistsSurvey(SubmitSurveyReqVO reqVO, List<QuestionDO> qst){
        Set<String> set1 = reqVO.getQstList().stream().map(p -> p.getQstCode()).collect(Collectors.toSet());
        Set<String> set2 = qst.stream().map(p -> p.getCode()).collect(Collectors.toSet());
        set1.removeAll(set2);
        if (set1.size() > 0) {
            throw exception(QUESTION_NOT_EXISTS_SURVEY);
        }
    }


    public void generateReport(Long answerId,List<ScaleReportAutoConfiguration.Grade> grades) {
        List<AnswerDetailDO> detailDOS = surveyAnswerDetailMapper.getByAnswerId(answerId);
        if (CollectionUtil.isEmpty(detailDOS)) {
            return;
        }
        Integer score = 0;
        for (AnswerDetailDO item : detailDOS) {
            if (Objects.isNull(item.getAnswer())) {
                score += item.getAnswer().getInt("val", 0);
            }
        }
        Integer finalScore = score;
        ScaleReportAutoConfiguration.Grade grade=grades.stream().filter(p->p.getBegin()<= finalScore && p.getEnd()>= finalScore).findFirst().get();
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.set("score",score);
        jsonObject.set("explain",grade.getExplain());
        jsonObject.set("shortExplain",grade.getShortExplain());
        jsonObject.set("begin",grade.getBegin());
        jsonObject.set("end",grade.getEnd());
        answerDO.setReprot(jsonObject.toString());
        surveyAnswerMapper.updateById(answerDO);
    }
}
