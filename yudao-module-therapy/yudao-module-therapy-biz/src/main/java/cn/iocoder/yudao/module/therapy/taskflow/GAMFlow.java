package cn.iocoder.yudao.module.therapy.taskflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import java.nio.charset.StandardCharsets;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;


public class GAMFlow extends BaseFlow{
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

    public Map<String, Object> auto_primary_troubles_qst(Map data, Task currentTask){
        // TODO init question instance
        data.put("instance_id", 123);
        return data;
    }

    public void submit_primary_troubles_qst(Map variables, Task currentTask){
        // TODO submit question instance
        Map stepVariables = (Map<String, Object>)variables.get("__current");
        List<String> troubles =  (List<String>) stepVariables.get("troubles");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable(currentTask.getProcessInstanceId(), "troubles", troubles);
    }

    public  Map<String, Object> auto_set_goal_qst(Map data, Task currentTask){
        // TODO init question instance
        data.put("instance_id", 123);
        return data;
    }

    public Map<String, Object> auto_my_actions_qst(Map data, Task currentTask){
        // TODO init question instance
        data.put("instance_id", 123);
        return data;
    }


}
