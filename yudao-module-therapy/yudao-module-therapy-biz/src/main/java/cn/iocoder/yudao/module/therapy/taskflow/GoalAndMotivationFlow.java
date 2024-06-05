package cn.iocoder.yudao.module.therapy.taskflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // TODO
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get("dayItemInstanceId");

        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_primary_troubles_qst(Map data, Task currentTask){
        int sourceTypeCustomize = 2;
        Long instance_id =  surveyService.initSurveyAnswer("GOAL_AND_MOTIVATION", sourceTypeCustomize);
        data.put("instance_id", instance_id);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable(processInstance.getId(), "survey_instance_id", instance_id);
        return data;
    }

    private Long submitSurveyData(Map variables){
        Map survey = (Map) variables.get("__survey");
        Map surveyData = (Map) survey.get("data");
        SubmitSurveyReqVO submitSurveyReqVO = new ObjectMapper().convertValue(surveyData, SubmitSurveyReqVO.class);
        return surveyService.submitSurveyForFlow(submitSurveyReqVO);

    }

    public void submit_primary_troubles_qst(Map variables, Task currentTask){
        // set troubles text, for later llm use
        Map stepVariables = (Map<String, Object>)variables.get("__current");
        List<String> troubles =  (List<String>) stepVariables.get("troubles");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable(currentTask.getProcessInstanceId(), "troubles", troubles);

        // submit survey data
        submitSurveyData(variables);
    }

    public  Map<String, Object> auto_set_goal_qst(Map data, Task currentTask){
        Map variables = getVariables();
        data.put("instance_id", (Long) variables.get("survey_instance_id"));
        // TODO read survey data from survey service
        Map instanceData = new HashMap<>();
        data.put("instance_data", instanceData);

        return data;
    }

    private Map getVariables(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.getVariables(processInstance.getId());
    }




    public void submit_set_goal_qst(Map variables, Task currentTask){
        // TODO submit question instance
        submitSurveyData(variables);
//        Map stepVariables = (Map<String, Object>)variables.get("__current");
//        String goal =  (String) stepVariables.get("goal");
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.setVariable(currentTask.getProcessInstanceId(), "goal", goal);
    }

    public Map<String, Object> auto_my_actions_qst(Map data, Task currentTask){
        // TODO init question instance
        Map variables = getVariables();
        data.put("instance_id", variables.get("survey_instance_id"));

        Map instanceData = new HashMap<>();
        data.put("instance_data", instanceData);
        return data;
    }

    public void submit_my_actions_qst(Map variables, Task currentTask) {
        // TODO submit question instance
        submitSurveyData(variables);
    }
}
