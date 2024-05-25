package cn.iocoder.yudao.module.therapy.taskflow;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.*;

/**
 * 目标与动机流程
 */
public class GoalAndMotivationFlow {
//    /**
//     *  Settings: {
//     *      "beginning_steps": [
//     *          {"type": "guide", "content": "请描述您的目标和动机"},
//     *          {"type": "guide", "content": "请至少写出3个"},
//     *          {"type": "user_input", "key": "my_objectives"}
//     *      ],
//     *      "ending_steps": [
//     *          {"type": "guide", "content": "感谢您的参与"}
//     *      ],
//     *      "agree_steps": [
//     *          {"type": "questions", "question_id": 3, "key": "investigation"},
//     *      ],
//     *      "disagree_steps": [
//     *          {"type": "guide", "content": "请你下次再来吧" }
//     *      ],
//     *  }
//     *
//     *
//     *
//     *
//     *
//     *
//     */
//    ProcessInstance processInstance;
//
//    ProcessEngine processEngine;
//
//    public GoalAndMotivationFlow(Long id, Map<String, Object> settings){
//        BpmnModel bpmnModel = createBPMNModel(id, settings);
//        processEngine = Engine.getEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.createDeployment()
//                .addBpmnModel("GoalAndMotivationFlow-" + String.valueOf(id) + ".bpmn", bpmnModel) // is this bpmn saved to database?
//                .deploy();
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", new HashMap<String, Object>());
//    }
//
//    public static void initFromData(){
//        // TODO
//    }
//
//
//
//    private FlowElement createCustomizedTask(Map<String, Object> step){
//        String type = (String) step.get("type");
//        switch (type) {
//            case "guide":
//                ServiceTask serviceTask = new ServiceTask();
//                serviceTask.setId(UUID.randomUUID().toString());
//                serviceTask.setName("guide-" + serviceTask.getId());
//                serviceTask.setImplementation("cn.iocoder.yudao.module.therapy.taskflow.servicetasks.GuideStepDelegate");
//                HashMap<String, List<ExtensionAttribute>> attr = new HashMap<>();
//                attr.put("content", Collections.singletonList(new ExtensionAttribute("content", (String) step.get("content"))));
//                serviceTask.setAttributes(attr);
//                return serviceTask;
//            case "user_input": {
//                String key = (String) step.get("key");
//                UserTask userTask = new UserTask();
//                userTask.setId(UUID.randomUUID().toString());
//                userTask.setName("user_input-" + userTask.getId());
////            userTask.setAssignee("${user}");
//                CustomProperty customProperty = new CustomProperty();
//                customProperty.setName(key);
//                userTask.setCustomProperties(Collections.singletonList(customProperty));
//                return userTask;
//            }
//            case "questions": {
//                String question_id = (String) step.get("question_id");
//                String key = (String) step.get("key");
//                UserTask userTask = new UserTask();
//                userTask.setId(UUID.randomUUID().toString());
//                userTask.setName("questions-" + userTask.getId());
//                CustomProperty customProperty1 = new CustomProperty();
//                customProperty1.setName(key);
//                CustomProperty customProperty2 = new CustomProperty();
//                customProperty2.setName(question_id);
//                userTask.setCustomProperties(Arrays.asList(customProperty1, customProperty2));
//                return userTask;
//            }
//        }
//        return null;
//    }
//
//    public Task getCurrentTask(){
//        TaskService taskService = processEngine.getTaskService();
//        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//        return currentTask;
//    }
//
//    public void completeTask(String taskId){
//        Task currentTask = getCurrentTask();
//        if(Objects.equals(taskId, currentTask.getId())){
//            TaskService taskService = processEngine.getTaskService();
//            taskService.complete(taskId);
//        }else{
//            throw new RuntimeException("Task id is not correct");
//        }
//    }
//
//    private BpmnModel createBPMNModel(Long id, Map<String, Object> settings) {
//        BpmnModel bpmnModel = new BpmnModel();
//        Process process = new Process();
//        String processName = "GoalAndMotivationFlow-" + id;
//        process.setId(processName);
//        process.setName(processName);
//        bpmnModel.addProcess(process);
//
//        // Start event
//        StartEvent startEvent = new StartEvent();
//        startEvent.setId("start");
//        process.addFlowElement(startEvent);
//        //
//        List<Map<String, Object>> beginning_steps = (List<Map<String, Object>>) settings.get("beginning_steps");
//        FlowElement lastElement = appendBatchTask(process, startEvent, beginning_steps);
//        // Add user agree task
//        UserTask userAgreeTask = new UserTask();
//        userAgreeTask.setId(UUID.randomUUID().toString());
//        userAgreeTask.setName("用户输入是否同意");
//        process.addFlowElement(userAgreeTask);
//        SequenceFlow sequenceFlow = createSequenceFlow(UUID.randomUUID().toString(), lastElement.getId(), userAgreeTask.getId());
//        process.addFlowElement(sequenceFlow);
//
//        // AI check task
//        ServiceTask aiCheckTask = new ServiceTask();
//        aiCheckTask.setId(UUID.randomUUID().toString());
//        aiCheckTask.setName("AI检查用户输入");
//        aiCheckTask.setImplementation("cn.iocoder.yudao.module.therapy.taskflow.servicetasks.AICheckUserAgreementStepDelegate");
//        process.addFlowElement(aiCheckTask);
//        sequenceFlow = createSequenceFlow(UUID.randomUUID().toString(), userAgreeTask.getId(), aiCheckTask.getId());
//        process.addFlowElement(sequenceFlow);
//
//
//        // Add exclusive gateway
//        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
//        exclusiveGateway.setId(UUID.randomUUID().toString());
//        exclusiveGateway.setName("用户是否同意");
//        process.addFlowElement(exclusiveGateway);
//
//
//        FlowElement aiToGateFlow = createSequenceFlow(UUID.randomUUID().toString(), aiCheckTask.getId(), exclusiveGateway.getId());
//        process.addFlowElement(aiToGateFlow);
//
//        // Add agree steps
//        List<Map<String, Object>> agree_steps = (List<Map<String, Object>>) settings.get("agree_steps");
//        List<Map<String, Object>> disagree_steps = (List<Map<String, Object>>) settings.get("disagree_steps");
//        FlowElement lastElementOfAgree = appendBatchTask(process, exclusiveGateway, agree_steps);
//        FlowElement lastElementOfDisagree = appendBatchTask(process,  exclusiveGateway, disagree_steps);
//        // Add ending steps
//        lastElement = appendBatchTask(process, (List<Map<String, Object>>) settings.get("ending_steps"),Arrays.asList(lastElementOfAgree, lastElementOfDisagree));
//
//
//        // End event
//        EndEvent endEvent = new EndEvent();
//        endEvent.setId("end");
//        process.addFlowElement(endEvent);
//
//        process.addFlowElement(createSequenceFlow(UUID.randomUUID().toString(), lastElement.getId(), endEvent.getId()));
//
//        // Exclusive gateway
//        return bpmnModel;
//
//    }
//
//    private FlowElement appendBatchTask(Process process, FlowElement lastElement, List<Map<String, Object>> steps ){
//        for(Map<String, Object> step: steps){
//            FlowElement task = createCustomizedTask(step );
//            process.addFlowElement(task);
//            SequenceFlow sequenceFlow = createSequenceFlow(UUID.randomUUID().toString(), lastElement.getId(), task.getId());
//            process.addFlowElement(sequenceFlow);
//            lastElement = task;
//        }
//        return lastElement;
//    }
//
//    private FlowElement appendBatchTask(Process process, List<Map<String, Object>> steps, List<FlowElement> lastElements){
//        FlowElement lastElement = lastElements.get(0);
//        int i = 0;
//        for(Map<String, Object> step: steps){
//            FlowElement task = createCustomizedTask(step );
//            process.addFlowElement(task);
//            if(i==0){
//                for(FlowElement element: lastElements){
//                    SequenceFlow sequenceFlow = createSequenceFlow(UUID.randomUUID().toString(), element.getId(), task.getId());
//                    process.addFlowElement(sequenceFlow);
//                }
//            }else{
//                SequenceFlow sequenceFlow = createSequenceFlow(UUID.randomUUID().toString(), lastElements.get(i-1).getId(), task.getId());
//                process.addFlowElement(sequenceFlow);
//            }
//            lastElement = task;
//            i += 1;
//        }
//        return lastElement;
//    }
//
//    private static SequenceFlow createSequenceFlow(String id, String sourceRef, String targetRef) {
//        SequenceFlow sequenceFlow = new SequenceFlow();
//        sequenceFlow.setId(id);
//        sequenceFlow.setSourceRef(sourceRef);
//        sequenceFlow.setTargetRef(targetRef);
//        return sequenceFlow;
//    }
}
