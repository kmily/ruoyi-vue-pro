package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.SurveyAnswerMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentScheduleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_ANSWER_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
public class StatServiceImpl implements StatService {
    @Resource
    private TreatmentScheduleMapper treatmentScheduleMapper;
    @Resource
    private SurveyAnswerMapper surveyAnswerMapper;

    @Override
    public List<ScheduleStateRespVO> StatSchedule(Integer dayNum, Long userId) {
        List<ScheduleStateRespVO> rsp = new ArrayList<>();
        for (int i = dayNum; i > 0; i--) {
            Map<String, Object> map = treatmentScheduleMapper.statSchedule(LocalDate.now().plusDays(-i), LocalDate.now().plusDays(1 - i), userId);
            if (Objects.nonNull(map)) {
                ScheduleStateRespVO vo = new ScheduleStateRespVO();
                vo.setDay(LocalDate.now().plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                vo.setNum((Long) map.getOrDefault("count", 0));
                if(vo.getNum()<=0){
                    continue;
                }
                Integer score = (Integer) map.getOrDefault("score", 0);
                vo.setScore(Math.round(score / vo.getNum()));
                rsp.add(vo);
            }
        }
        return rsp;
    }

    @Override
    public Map<String, String> getScaleReport(Long answerId, Long userId) {
        SurveyAnswerDO answerDO = surveyAnswerMapper.selectById(answerId);
        if (Objects.isNull(answerDO)) {
            throw exception(SURVEY_ANSWER_NOT_EXISTS);
        }
        LocalDate begin = LocalDate.of(answerDO.getCreateTime().getYear(), answerDO.getCreateTime().getMonth(), answerDO.getCreateTime().getDayOfMonth());
        LocalDate end = begin.plusDays(1);
        List<SurveyAnswerDO> answerDOS = surveyAnswerMapper.selectBySurveysAndDate(userId, begin, end
                , Arrays.asList(SurveyType.GAD7_SCALE.getType(), SurveyType.PHQ9_SCALE.getType()
                        , SurveyType.MOOD_SCALE.getType(), SurveyType.ISI_SCALE.getType()));
        Map<String, String> map = new HashMap<>();
        for (SurveyAnswerDO item : answerDOS) {
            map.put(SurveyType.getByType(item.getSurveyType()).getCode(), item.getReprot());
        }
        return map;
    }

    @Override
    public Map<String, List<KeyValue>> getScaleChart(Long userId) {
        List<SurveyAnswerDO> answerDOS = surveyAnswerMapper.selectBySurveyTypeAndUserId(getLoginUserId(), Arrays.asList(SurveyType.GAD7_SCALE.getType(), SurveyType.PHQ9_SCALE.getType()
                , SurveyType.MOOD_SCALE.getType(), SurveyType.ISI_SCALE.getType()));
        Map<String, List<KeyValue>> res = new HashMap<>();
        if (CollectionUtil.isNotEmpty(answerDOS)) {
            for (SurveyAnswerDO item : answerDOS) {
                if (item.getSurveyType().equals(SurveyType.MOOD_SCALE.getType())) {
                    //积极
                    KeyValue<LocalDateTime, Long> keyValue = new KeyValue<>();
                    keyValue.setKey(item.getCreateTime());
                    keyValue.setValue((new JSONObject(item.getReprot())).getLong("positiveScore", 0L));
                    List<KeyValue> list1 = res.getOrDefault("positiveScore", new ArrayList<>());
                    list1.add(keyValue);
                    res.put("positiveScore", list1);
                    //消极
                    KeyValue<LocalDateTime, Long> keyValue1 = new KeyValue<>();
                    keyValue1.setKey(item.getCreateTime());
                    keyValue1.setValue((new JSONObject(item.getReprot())).getLong("passiveScore", 0L));
                    List<KeyValue> list2 = res.getOrDefault("passiveScore", new ArrayList<>());
                    list2.add(keyValue1);
                    res.put("passiveScore", list2);
                    continue;
                }
                KeyValue<LocalDateTime, Long> keyValue = new KeyValue<>();
                keyValue.setKey(item.getCreateTime());
                keyValue.setValue((new JSONObject(item.getReprot())).getLong("score", 0L));
                List<KeyValue> list = res.getOrDefault(SurveyType.getByType(item.getSurveyType()).getCode(), new ArrayList<>());
                list.add(keyValue);
                res.put(SurveyType.getByType(item.getSurveyType()).getCode(), list);

            }
        }

        return res;
    }

    @Override
    public List<SurveyAnswerDO> getAnswerList(Long userId, LocalDate begin, LocalDate end,List<Integer> types) {
        return surveyAnswerMapper.selectBySurveysAndDate(userId,begin,end,types);
    }

    @Override
    public Map<String, List<JSONObject>> getStrategyCard(Long userId) {
        return null;
    }

    @Override
    public List<SurveyAnswerDO> getScaleList(Long userId) {

        return surveyAnswerMapper.selectBySurveyTypeAndUserId(userId, Arrays.asList(SurveyType.PHQ9_SCALE.getType()));
    }
}
