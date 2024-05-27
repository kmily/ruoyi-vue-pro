package cn.iocoder.yudao.module.therapy.taskflow;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

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
    ProcessInstance processInstance;

    ProcessEngine processEngine;


    public GoalAndMotivationFlow(){
        processEngine = new Engine().getEngine();
    }



    @Override
    public String deploy(Long id, Map<String, Object> settings){
        settings = new HashMap();
        settings.put("beginning_steps", Arrays.asList(
                Map.of("type", "guide", "content", "请描述您的目标和动机"),
                Map.of("type", "guide", "content", "请至少写出3个"),
                Map.of("type", "user_input", "key", "my_objectives")
        ));
        settings.put("ending_steps", Arrays.asList(
                Map.of("type", "guide", "content", "感谢您的参与")
        ));
        settings.put("agree_steps", Arrays.asList(
                Map.of("type", "questions", "question_id", 3, "key", "investigation")
        ));
        settings.put("disagree_steps", Arrays.asList(
                Map.of("type", "guide", "content", "请你下次再来吧")
        ));

        BpmnModel bpmnModel = createBPMNModel(id, settings);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addBpmnModel("GoalAndMotivationFlow-" + String.valueOf(id) + ".bpmn", bpmnModel)
                .deploy();

        return "GoalAndMotivationFlow-" + String.valueOf(id);
    }

    @Override
    public String createProcessInstance(String bmpnName){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processInstance = runtimeService.startProcessInstanceByKey(bmpnName, new HashMap<String, Object>());
        return processInstance.getId();
    }

    @Override
    public void loadProcessInstance(String processInstanceId){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    String generateTaskId(){
        return "name-" + UUID.randomUUID().toString();
    }


    private FlowElement createCustomizedTask(Map<String, Object> step){
        String type = (String) step.get("type");
        switch (type) {
            case "guide":
                ServiceTask serviceTask = new ServiceTask();
                serviceTask.setId(generateTaskId());
                serviceTask.setName("guide-" + serviceTask.getId());
                serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
                serviceTask.setImplementation("cn.iocoder.yudao.module.therapy.taskflow.servicetasks.GuideStepDelegate");

                // Create a FieldExtension to set the content as a variable on the DelegateExecution object
                FieldExtension fieldExtension = new FieldExtension();
                fieldExtension.setFieldName("content");
                fieldExtension.setStringValue((String) step.get("content"));
                serviceTask.getFieldExtensions().add(fieldExtension);

                return serviceTask;
            case "user_input": {
                String key = (String) step.get("key");
                UserTask userTask = new UserTask();
                userTask.setId(generateTaskId());
                userTask.setName("user_input-" + userTask.getId());
//            userTask.setAssignee("${user}");
                CustomProperty customProperty = new CustomProperty();
                customProperty.setName(key);
                userTask.setCustomProperties(Collections.singletonList(customProperty));
                return userTask;
            }
            case "questions": {
                String question_id = String.valueOf(step.get("question_id")) ;
                String key = (String) step.get("key");
                UserTask userTask = new UserTask();
                userTask.setId(generateTaskId());
                userTask.setName("questions-" + userTask.getId());
                CustomProperty customProperty1 = new CustomProperty();
                customProperty1.setName(key);
                CustomProperty customProperty2 = new CustomProperty();
                customProperty2.setName(question_id);
                userTask.setCustomProperties(Arrays.asList(customProperty1, customProperty2));
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
        UserTask userAgreeTask = new UserTask();
        userAgreeTask.setId(generateTaskId());
        userAgreeTask.setName("用户输入是否同意");
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
    private FlowElement appendWaitSignalEvent(Process process, FlowElement element){
        // Signal event
        IntermediateCatchEvent signalEvent = new IntermediateCatchEvent();
        String signalEventId = generateTaskId();
        signalEvent.setId(signalEventId);
        signalEvent.setName(WAIT_FOR_SIGNAL_TASK_NAME);
        SignalEventDefinition signalEventDefinition = new SignalEventDefinition();
        signalEventDefinition.setSignalRef("NEXT"); // replace with your signal name
        signalEvent.addEventDefinition(signalEventDefinition);

        process.addFlowElement(signalEvent);
        process.addFlowElement(createSequenceFlow(generateTaskId(), element.getId(), signalEvent.getId()));
        return signalEvent;
    }

    private FlowElement appendBatchTask(Process process, FlowElement lastElement, List<Map<String, Object>> steps ){
        for(Map<String, Object> step: steps){
            FlowElement task = createCustomizedTask(step);
            process.addFlowElement(task);
            SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), lastElement.getId(), task.getId());
            process.addFlowElement(sequenceFlow);

            lastElement = appendWaitSignalEvent(process, task);
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
            lastElement = appendWaitSignalEvent(process, task);
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
        result.put("task_id", currentTask.getId());
        result.put("task_name", currentTask.getName());
        result.put("data", currentTask.getProcessVariables().get("ServerResp"));
        RuntimeService runtimeService = processEngine.getRuntimeService();
        if(currentTask.getName().equals(WAIT_FOR_SIGNAL_TASK_NAME)){
            runtimeService.signalEventReceived("NEXT");
        }

        runtimeService.removeVariable(currentTask.getExecutionId(), "ServerResp");
        return result;
    }
}
