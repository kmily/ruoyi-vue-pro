package cn.iocoder.yudao.module.therapy.taskflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 目标与动机流程
 */

@Component
public class GoalAndMotivationFlow implements BaseFlow{
    /**
     *  Settings: {
     *      "beginning_steps": [
     *          {"type": "guide", "content": "请描述您的目标和动机"},
     *          {"type": "guide", "content": "请至少写出3个"},
     *          {"type": "user_input", "key": "my_objectives"}
     *      ],
     *      "ending_steps": [
     *          {"type": "guide", "content": "感谢您的参与"}
     *      ],
     *      "agree_steps": [
     *          {"type": "questions", "question_id": 3, "key": "investigation"},
     *      ],
     *      "disagree_steps": [
     *          {"type": "guide", "content": "请你下次再来吧" }
     *      ],
     *  }
     *
     *
     *
     *
     *
     *
     */

    String USER_CONTINUE_TASK_NAME = "UserContinueTask";
    String USER_GUIDE_TEXT_TASK_NAME = "UserGuideText";
    String USER_INPUT_TASK_NAME = "UserInputTask"; // 需要用户输入

    ProcessInstance processInstance;

    ProcessEngine processEngine;


    public GoalAndMotivationFlow(){
        processEngine = new Engine().getEngine();
    }

    private Map getDefaultSettings(){
        HashMap settings = new HashMap();
        Map<String, Object> guide1 = new HashMap<>();
        guide1.put("type", "guide");
        guide1.put("content", "请描述您的目标和动机");

        Map<String, Object> guide2 = new HashMap<>();
        guide2.put("type", "guide");
        guide2.put("content", "请至少写出3个");

        Map<String, Object> userInput = new HashMap<>();
        userInput.put("type", "user_input");
        userInput.put("key", "my_objectives");

        settings.put("beginning_steps", Arrays.asList(guide1, guide2, userInput));

        Map<String, Object> endingStep = new HashMap<>();
        endingStep.put("type", "guide");
        endingStep.put("content", "感谢您的参与");

        settings.put("ending_steps", Arrays.asList(endingStep));

        Map<String, Object> agreeStep = new HashMap<>();
        agreeStep.put("type", "questions");
        agreeStep.put("question_id", 3);
        agreeStep.put("key", "investigation");

        settings.put("agree_steps", Arrays.asList(agreeStep));

        Map<String, Object> disagreeStep = new HashMap<>();
        disagreeStep.put("type", "guide");
        disagreeStep.put("content", "请你下次再来吧");

        settings.put("disagree_steps", Arrays.asList(disagreeStep));
        return settings;
    }


    @Override
    public String deploy(Long id, Map<String, Object> settings){
        settings = getDefaultSettings();
        BpmnModel bpmnModel = createBPMNModel(id, settings);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addBpmnModel("GoalAndMotivationFlow-" + String.valueOf(id) + ".bpmn", bpmnModel)
                .deploy();

        System.out.println(new String(new BpmnXMLConverter().convertToXML(bpmnModel), StandardCharsets.UTF_8));
//        processEngine.getRepositoryService().getBpmnModel(bmpnName);
//                .latestVersion()
//                .singleResult().getId());
//        new BpmnXMLConverter().convertToXML(tmpModel);

            return "GoalAndMotivationFlow-" + String.valueOf(id);
    }

    @Override
    public String createProcessInstance(String bmpnName){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.createProcessInstanceBuilder().processDefinitionKey(bmpnName);
        processInstance = runtimeService.startProcessInstanceByKey(bmpnName, new HashMap<String, Object>());
        return processInstance.getId();
    }

    @Override
    public void loadProcessInstance(String processInstanceId){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public void userSubmit(String taskId, Map<String, Object> variables) {
        Task task = getCurrentTask();
        if(taskId.equals(task.getId())) {
            TaskService taskService = processEngine.getTaskService();
            taskService.complete(taskId, variables);
        }else{
            throw new RuntimeException("Task id is not correct");
        }
    }

    private void bindContentToUserTask(UserTask userTask, Map content){
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setName("content"); // The name of your custom attribute
        extensionElement.setNamespacePrefix("flowable"); // Namespace prefix
        extensionElement.setNamespace("http://flowable.org/bpmn"); // Namespace
        // please convert content to json text
        String contentStr = "";
        try {
            contentStr = new ObjectMapper().writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        extensionElement.setElementText(contentStr); // The value of your custom attribute
        List<ExtensionElement> etsElems = new ArrayList<>();
        etsElems.add(extensionElement);
        userTask.setExtensionElements(Collections.singletonMap("content", etsElems));
    }

    String generateTaskId(){
        return "name-" + UUID.randomUUID().toString();
    }


    private FlowElement createCustomizedTask(Map<String, Object> step){
        String type = (String) step.get("type");
        switch (type) {
            case "guide":
                UserTask guideTask = new UserTask();
                guideTask.setId(generateTaskId());
                guideTask.setName(USER_GUIDE_TEXT_TASK_NAME);

                ExtensionElement extensionElement = new ExtensionElement();
                extensionElement.setName("content"); // The name of your custom attribute
                extensionElement.setNamespacePrefix("flowable"); // Namespace prefix
                extensionElement.setNamespace("http://flowable.org/bpmn"); // Namespace
                extensionElement.setElementText((String) step.get("content")); // The value of your custom attribute
                List<ExtensionElement> etsElems = new ArrayList<>();
                etsElems.add(extensionElement);
                guideTask.setExtensionElements(Collections.singletonMap("content", etsElems));
                return guideTask;
            case "user_input": {
                String key = (String) step.get("key");
                UserTask userTask = new UserTask();
                userTask.setId(generateTaskId());
                userTask.setName("user_input-" + userTask.getId());
                step.put("item_type", "user_input");
                bindContentToUserTask(userTask, step);
                return userTask;
            }
            case "questions": {
                String question_id = String.valueOf(step.get("question_id")) ;
                String key = (String) step.get("key");
                UserTask userTask = new UserTask();
                userTask.setId(generateTaskId());
                userTask.setName("questions-" + userTask.getId());
                step.put("item_type", "questions");
                bindContentToUserTask(userTask, step);
                return userTask;
            }
        }
        throw new RuntimeException("Step type is not correct: " + type);
    }

    public Task getCurrentTask(){
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        return currentTask;
    }

    public void completeTask(String taskId){
        Task currentTask = getCurrentTask();
        if(Objects.equals(taskId, currentTask.getId())){
            TaskService taskService = processEngine.getTaskService();
            taskService.complete(taskId);
        }else{
            throw new RuntimeException("Task id is not correct");
        }
    }

    public UserTask createUserInputTask(Map<String, Object> content){
        UserTask userInputTask = new UserTask();
        userInputTask.setId(generateTaskId());
        userInputTask.setName(USER_INPUT_TASK_NAME);
        bindContentToUserTask(userInputTask, content);
        return userInputTask;
    }

    public BpmnModel createBPMNModel(Long id, Map<String, Object> settings) {
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        String processName = "GoalAndMotivationFlow-" + id;
        process.setId(processName);
        process.setName(processName);
        bpmnModel.addProcess(process);

        // Start event
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        process.addFlowElement(startEvent);
        //
        List<Map<String, Object>> beginning_steps = (List<Map<String, Object>>) settings.get("beginning_steps");
        FlowElement lastElement = appendBatchTask(process, startEvent, beginning_steps);

        // Add user agree task
        HashMap<String, Object> userAgreeContent = new HashMap<>();
        userAgreeContent.put("item_type", "user_agree");
        userAgreeContent.put("content", "请问您还继续吗？");
        userAgreeContent.put("need_feedback", true);

        UserTask userAgreeTask = createUserInputTask(userAgreeContent);
        process.addFlowElement(userAgreeTask);
        SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), lastElement.getId(), userAgreeTask.getId());
        process.addFlowElement(sequenceFlow);

        // AI check task
        ServiceTask aiCheckTask = new ServiceTask();
        aiCheckTask.setId(generateTaskId());
        aiCheckTask.setName("AI检查用户输入");
        aiCheckTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        aiCheckTask.setImplementation("cn.iocoder.yudao.module.therapy.taskflow.servicetasks.AICheckUserAgreementStepDelegate");
        process.addFlowElement(aiCheckTask);
        sequenceFlow = createSequenceFlow(generateTaskId(), userAgreeTask.getId(), aiCheckTask.getId());
        process.addFlowElement(sequenceFlow);


        // Add exclusive gateway
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(generateTaskId());
        exclusiveGateway.setName("用户是否同意");
        process.addFlowElement(exclusiveGateway);


        FlowElement aiToGateFlow = createSequenceFlow(generateTaskId(), aiCheckTask.getId(), exclusiveGateway.getId());
        process.addFlowElement(aiToGateFlow);

        // Add agree steps
        List<Map<String, Object>> agree_steps = (List<Map<String, Object>>) settings.get("agree_steps");
        List<Map<String, Object>> disagree_steps = (List<Map<String, Object>>) settings.get("disagree_steps");

        FlowElement lastElementOfAgree = appendBatchTask(process, exclusiveGateway, agree_steps);
        FlowElement lastElementOfDisagree = appendBatchTask(process,  exclusiveGateway, disagree_steps);
        // Add ending steps
        lastElement = appendBatchTask(process, (List<Map<String, Object>>) settings.get("ending_steps"),Arrays.asList(lastElementOfAgree, lastElementOfDisagree));


        // End event
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        process.addFlowElement(endEvent);

        process.addFlowElement(createSequenceFlow(generateTaskId(), lastElement.getId(), endEvent.getId()));

        // Exclusive gateway
        return bpmnModel;

    }

    String WAIT_FOR_SIGNAL_TASK_NAME = "Wait for Signal";
    private FlowElement appendUserContinueTask(Process process, FlowElement element){
        // User Continue Task
        UserTask userContinueTask = new UserTask();
        String userContinueTaskId = generateTaskId();
        userContinueTask.setId(userContinueTaskId);
        userContinueTask.setName(USER_CONTINUE_TASK_NAME);

        process.addFlowElement(userContinueTask);
        process.addFlowElement(createSequenceFlow(generateTaskId(), element.getId(), userContinueTask.getId()));
        return userContinueTask;
    }

    private FlowElement appendBatchTask(Process process, FlowElement lastElement, List<Map<String, Object>> steps ){
        for(Map<String, Object> step: steps){
            FlowElement task = createCustomizedTask(step);
            process.addFlowElement(task);
            SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), lastElement.getId(), task.getId());
            process.addFlowElement(sequenceFlow);

            lastElement = task;
        }
        return lastElement;
    }

    private FlowElement appendBatchTask(Process process, List<Map<String, Object>> steps, List<FlowElement> lastElements){
        FlowElement lastElement = lastElements.get(0);
        int i = 0;
        for(Map<String, Object> step: steps){
            FlowElement task = createCustomizedTask(step );
            process.addFlowElement(task);
            if(i==0){
                for(FlowElement element: lastElements){
                    SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), element.getId(), task.getId());
                    process.addFlowElement(sequenceFlow);
                }
            }else{
                SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), lastElements.get(i-1).getId(), task.getId());
                process.addFlowElement(sequenceFlow);
            }
            lastElement = task;
            i += 1;
        }
        return lastElement;
    }

    private static SequenceFlow createSequenceFlow(String id, String sourceRef, String targetRef) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(id);
        sequenceFlow.setSourceRef(sourceRef);
        sequenceFlow.setTargetRef(targetRef);
        return sequenceFlow;
    }

    @Override
    public Map run(){
        Task currentTask = getCurrentTask();
        HashMap result = new HashMap();
        result.put("__task_id", currentTask.getId());
        result.put("__task_name", currentTask.getName());
        if(currentTask.getName().equals(USER_GUIDE_TEXT_TASK_NAME)){
            String activityId = currentTask.getTaskDefinitionKey(); // Get the task's definition key
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(currentTask.getProcessDefinitionId());
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);

            ExtensionElement ele =  userTask.getExtensionElements().get("content").get(0);
            String guideText = ele.getElementText();
            result.put("item_type", "guide");
            result.put("content", guideText);
            TaskService taskService = processEngine.getTaskService();
            taskService.complete(currentTask.getId());
        }else if (currentTask.getName().equals(USER_INPUT_TASK_NAME)){

            String activityId = currentTask.getTaskDefinitionKey(); // Get the task's definition key
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(currentTask.getProcessDefinitionId());
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);

            ExtensionElement ele =  userTask.getExtensionElements().get("content").get(0);
            String content = ele.getElementText();
            Map<String, Object> data = new HashMap();
            try {
                data = new ObjectMapper().readValue(content, Map.class);
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    result.put(entry.getKey(), entry.getValue());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }else{

            String activityId = currentTask.getTaskDefinitionKey(); // Get the task's definition key
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(currentTask.getProcessDefinitionId());
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);

            ExtensionElement ele =  userTask.getExtensionElements().get("content").get(0);
            String content = ele.getElementText();
            Map<String, Object> data = new HashMap();
            try {
                data = new ObjectMapper().readValue(content, Map.class);
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    result.put(entry.getKey(), entry.getValue());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return result;
    }
}
