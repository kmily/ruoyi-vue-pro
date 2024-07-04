package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import org.flowable.task.api.Task;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;


@Component
public class MoodScoreFlow extends BaseFlow {
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    @Resource
    private TreatmentService treatmentService;

    public MoodScoreFlow(ProcessEngine engine) {
        super(engine);
    }


    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/mood_score.json", settings);
    }

//
//    {"step_type": "guide_language", "step_data": {"content":  "为什么要使用情绪尺子？"}},
//    {"step_type": "guide_language", "step_data": {"content":  "评估你的情绪有很多好处。首先，它可以帮助你发现情绪变化的模式。你可能会开始注意到，某些活动、人或情况会让你的情绪分数上升或下降。这些洞察可以引导你做出改变，比如避开使你情绪低落的因素，或者寻找更多使你感觉好转的活动。"}},
//    {"step_type": "guide_language", "step_data": {"content":  "其次，通过跟踪情绪，你可以开始理解哪些策略对于提高你的情绪有效，哪些不是。这有助于你更好地管理情绪，学习如何自我调节，从而在面对挑战时更加有力。"}},
//    {"step_type": "guide_language", "step_data": {"content":  "最后，这还可以帮助你和治疗师或支持你的人更有效地交流。有了具体的情绪评分，你可以更清晰地表达自己的感受，让他们更好地理解你的需求。）"}}

    @Override
    public String getProcessName(Long id) {
        return "MOOD_SCORE-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentService.finishDayItemInstance(dayItemInstanceId);
    }



    @Override
    public Map fireEvent(Long dayitemInstanceId, String eventName){
        if(eventName == "why_user_mood_ruler"){}
        HashMap result = new HashMap();
        result.put("__step_id", "multi_guide_languages" );
        result.put("__step_name", "multi_guide_languages");
        result.put("step_type", "multi_guide_languages");
        Map stepData = new HashMap();
        stepData.put("content", Arrays.asList(
                "评估你的情绪有很多好处。首先，它可以帮助你发现情绪变化的模式。你可能会开始注意到，某些活动、人或情况会让你的情绪分数上升或下降。这些洞察可以引导你做出改变，比如避开使你情绪低落的因素，或者寻找更多使你感觉好转的活动。",
                "其次，通过跟踪情绪，你可以开始理解哪些策略对于提高你的情绪有效，哪些不是。这有助于你更好地管理情绪，学习如何自我调节，从而在面对挑战时更加有力。",
                "最后，这还可以帮助你和治疗师或支持你的人更有效地交流。有了具体的情绪评分，你可以更清晰地表达自己的感受，让他们更好地理解你的需求。"
        ));
        result.put("step_data", stepData);
        return result;
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
        List<String> replies = (List<String>) data.get(String.valueOf(moodScore));
        Random rand = new Random();
        String randReply = replies.get(rand.nextInt(replies.size()));
        result.put("content", randReply);
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
