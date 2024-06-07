package cn.iocoder.yudao.module.therapy.taskflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.engine.*;

import java.nio.charset.StandardCharsets;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.QUESTION_NOT_EXISTS_SURVEY;
import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.TREATMENT_DAYITEM_STEP_PARAMS_ERROR;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;


@Component
public class GoalAndMotivationFlow extends BaseFlow{
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    public GoalAndMotivationFlow(ProcessEngine engine) {
        super(engine);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        // read settings from resources/goal_and_motivation_settings.json
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream("/goal_and_motivation_flow.json")) {
            settings = objectMapper.readValue(inputStream, Map.class);
            // use settings
        } catch (IOException e) {
            // handle exception
            throw new RuntimeException("Failed to read settings from resources/goal_and_motivation_settings.json");
        }
        BpmnModel bpmnModel = createBPMNModel(id, settings);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addBpmnModel("GoalAndMotivationFlow-" + String.valueOf(id) + ".bpmn", bpmnModel)
                .deploy();
        System.out.println(new String(new BpmnXMLConverter().convertToXML(bpmnModel), StandardCharsets.UTF_8));
        return getProcessName(id);
    }


    @Override
    public String getProcessName(Long id) {
        return "GOAL_AND_MOTIVATION-"  + String.valueOf(id);
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get("dayItemInstanceId");

        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_primary_troubles_qst(Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(processInstance.getId(), "survey_instance_id");
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.PROBLEM_GOAL_MOTIVE.getCode(), 2);
            runtimeService.setVariable(processInstance.getId(), "survey_instance_id", instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }


    public void submit_primary_troubles_qst(DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        // set troubles text, for later llm use
        Map stepVariables = submitReqVO.getStep_data().getCurrent();
        List<String> troubles;
        try {
            troubles =  (List<String>) stepVariables.get("troubles");
        } catch (Exception e) {
            throw exception(TREATMENT_DAYITEM_STEP_PARAMS_ERROR);
        }
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable(currentTask.getProcessInstanceId(), "troubles", troubles);
        // submit survey data
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public  Map<String, Object> auto_set_goal_qst(Map data, Task currentTask){
        Map variables = getVariables();
        data.put("instance_id", (Long) variables.get("survey_instance_id"));
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get("survey_instance_id"));
        data.put("instance_data", instanceData);
        return data;
    }

    private Map getVariables(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.getVariables(processInstance.getId());
    }




    public void submit_set_goal_qst(DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public Map<String, Object> auto_my_actions_qst(Map data, Task currentTask){
        Map variables = getVariables();
        data.put("instance_id", variables.get("survey_instance_id"));

        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get("survey_instance_id"));
        data.put("instance_data", instanceData);
        return data;
    }

    public void submit_my_actions_qst(DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
