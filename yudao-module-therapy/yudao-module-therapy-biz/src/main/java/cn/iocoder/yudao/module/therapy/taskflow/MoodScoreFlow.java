package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.task.api.Task;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;


@Component
public class MoodScoreFlow extends BaseFlow {
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    public MoodScoreFlow(ProcessEngine engine) {
        super(engine);
    }


    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/mood_score.json");
    }



    @Override
    public String getProcessName(Long id) {
        return "MOOD_SCORE-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_mood_ruler_qst(Container container,Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.MOOD_MARK.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_mood_ruler_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        int moodScore = (int) submitReqVO.getStep_data().getStore().get("moodScore");
        runtimeService.setVariable(container.getProcessInstanceId(), "moodScore", moodScore);
        SubmitSurveyReqVO survey = submitReqVO.getStep_data().getSurvey();
        surveyService.submitSurveyForFlow(survey);
    }

    public Map auto_mood_respond(Container container,Map data, Task currentTask){
        Map variables = super.getVariables(container);
        int moodScore = (int) variables.get("moodScore");
        Map result = new HashMap<>();
        result.put("content", data.get(String.valueOf(moodScore)));
        return result;
    }

    public Map auto_mood_diary_qst(Container container,Map data, Task currentTask){
        String DIARY_SURVEY_INSTANCE_ID = "survey_diary_instance_id";
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), DIARY_SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.MOOD_DIARY.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), DIARY_SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_mood_diary_qst(Container container,DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        SubmitSurveyReqVO survey = submitReqVO.getStep_data().getSurvey();
        surveyService.submitSurveyForFlow(survey);
    }
}
