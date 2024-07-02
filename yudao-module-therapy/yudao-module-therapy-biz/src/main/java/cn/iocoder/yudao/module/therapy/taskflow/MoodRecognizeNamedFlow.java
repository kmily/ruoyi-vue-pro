package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.flowable.task.api.Task;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;

@Component
public class MoodRecognizeNamedFlow extends BaseFlow{
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    @Resource
    private TreatmentService treatmentService;

    public MoodRecognizeNamedFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "MoodRecognizeNamed-" + id;
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/mood_recognize.json", settings);
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentService.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_mood_categories(Container container,Map data, Task currentTask){
        return data;
    }

    public Map<String, Object> auto_mood_categories_qst(Container container,Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.MOOD_RECOGNITION.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_mood_categories_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public Map<String, Object> auto_mood_describe_qst(Container container,Map data, Task currentTask){
        Map variables = getVariables(container);
        Long instance_id = (Long) variables.get(SURVEY_INSTANCE_ID);
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get(SURVEY_INSTANCE_ID));
        data.put("instance_data", instanceData);
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_mood_describe_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }


}
