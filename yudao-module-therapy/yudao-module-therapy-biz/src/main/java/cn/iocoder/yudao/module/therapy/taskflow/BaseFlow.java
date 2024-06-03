package cn.iocoder.yudao.module.therapy.taskflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseFlow {
    ProcessInstance processInstance;

    ProcessEngine processEngine;

    public BaseFlow(){
        processEngine = new Engine().getEngine();
    }

    public Task getCurrentTask(){
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = (Task) taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        return currentTask;
    }

    /**
     * 运行流程, 返回下一步的信息
     * @return
     */
    public Map run(){
        Task currentTask = getCurrentTask();
        HashMap result = new HashMap();
        result.put("__step_id", currentTask.getId());
        result.put("__step_name", currentTask.getName());

        String activityId = currentTask.getTaskDefinitionKey(); // Get the task's definition key
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(currentTask.getProcessDefinitionId());
        UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);

        ExtensionElement ele =  userTask.getExtensionElements().get("bind").get(0);
        String content = ele.getElementText();
        Map<String, Object> data = new HashMap();
        try {
            data = new ObjectMapper().readValue(content, Map.class);
            result.put("step_type", data.get("step_type"));
            Method method = this.getClass().getMethod("auto_" + (String) data.get("step_type"), Map.class, Task.class);
            Map stepResult = (Map) method.invoke(this, data.get("step_data"), currentTask);
            result.put("step_data", stepResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 引导语
     * @param data
     * @return
     */
    public Map auto_guide_language(Map data, Task currentTask){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(currentTask.getId());
        return data;
    }

    public Map auto_do_you_agree(Map data, Task currentTask){
        TaskService taskService = processEngine.getTaskService();
        return data;
    }

    Map<String, Object> getBindData(Task task){
        String activityId = task.getTaskDefinitionKey(); // Get the task's definition key
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
        UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);
        ExtensionElement ele =  userTask.getExtensionElements().get("bind").get(0);
        String content = ele.getElementText();
        Map<String, Object> data = new HashMap();
        try {
            data = new ObjectMapper().readValue(content, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public void submit_do_you_agree(Map variables, Task currentTask){
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> currentVariable = (Map<String, Object>)variables.get("__current");
        Map bindData = getBindData(currentTask);
        String agreeKey = bindData.get("variable").toString();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable( processInstance.getId(), agreeKey, (boolean) currentVariable.get(agreeKey));
        taskService.complete(currentTask.getId());
    }


    public void userSubmit(String taskId, Map<String, Object> variables) {
        Task task = getCurrentTask();
        if(taskId.equals(task.getId())) {
            String activityId = task.getTaskDefinitionKey(); // Get the task's definition key
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);
            ExtensionElement ele =  userTask.getExtensionElements().get("bind").get(0);
            String content = ele.getElementText();
            Map<String, Object> data = new HashMap();
            try {
                data = new ObjectMapper().readValue(content, Map.class);
                Method method = this.getClass().getMethod("submit_" + (String) data.get("step_type"), Map.class, Task.class);
                method.invoke(this, variables, task);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            TaskService taskService = processEngine.getTaskService();
            taskService.complete(taskId, variables);
        }else{
            throw new RuntimeException("Task id is not correct");
        }
    }

    /**
     * 创建流程实例
     * @param bmpnName
     * @return
     */
    public String createProcessInstance(String bmpnName){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.createProcessInstanceBuilder().processDefinitionKey(bmpnName);
        processInstance = runtimeService.startProcessInstanceByKey(bmpnName, new HashMap<String, Object>());
        return processInstance.getId();
    }

    /**
     * 加载流程实例
     * @param processInstanceId
     */
    public void loadProcessInstance(String processInstanceId){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }


    SequenceFlow createSequenceFlow(String id, String sourceRef, String targetRef) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(id);
        sequenceFlow.setSourceRef(sourceRef);
        sequenceFlow.setTargetRef(targetRef);
        return sequenceFlow;
    }


    void bindContentToUserTask(UserTask userTask, Map content){
        List<ExtensionElement> etsElems = createExtentionElement("bind", content);
        userTask.setExtensionElements(Collections.singletonMap("bind", etsElems));
    }

    String generateTaskId(){
        return "name-" + UUID.randomUUID().toString();
    }

    public abstract String  getProcessName(Long id);

    /**
     * 根据Settings创建BPMNModel
     * @param id 使用TreatmentFlowDayitemId
     * @param settings
     * @return
     */
    BpmnModel createBPMNModel(Long id, Map<String, Object> settings) {
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        String processName = getProcessName(id);
        process.setId(processName);
        process.setName(processName);
        bpmnModel.addProcess(process);

        Map<String, List<FlowElement>> stepGroups = new HashMap<>();
        Map<String, Object> groupNodes = (Map<String, Object>) settings.get("group_nodes");
        for(Map.Entry<String, Object> entry: groupNodes.entrySet()){
            String groupKey = entry.getKey();
            List<Map<String, Object>> groupSteps = (List<Map<String, Object>>) entry.getValue();
            List<FlowElement> steps = groupSteps.stream().map(this::convertToFlowElement).collect(Collectors.toList());
            stepGroups.put(groupKey, steps);
            FlowElement lastStep = null;
            for(FlowElement step: steps){
                process.addFlowElement(step);
                if (lastStep != null){
                    addSequenceRelation(process, lastStep, step);
                }
                lastStep = step;
            }

        }

        List<Map<String, String>> groupRelations = (List<Map<String, String>>) settings.get("group_relations");
        for(Map<String, String> groupRelation: groupRelations){
            String sourceGroup = groupRelation.get("source");
            String targetGroup = groupRelation.getOrDefault("target", "");
            String flowType = groupRelation.getOrDefault("type", "Sequence");
            if(flowType.equals("Sequence")){
                addSequenceRelation(process, stepGroups.get(sourceGroup), stepGroups.get(targetGroup));
            }else if (flowType.equals("ExclusiveGate")){
                String nextTrue = groupRelation.getOrDefault("next_true", "");
                String nextFalse = groupRelation.getOrDefault("next_false", "");
                String variableName = groupRelation.getOrDefault("variable", "");
                addExclusiveGateRelation(process, stepGroups.get(sourceGroup),
                        stepGroups.get(nextTrue), stepGroups.get(nextFalse),
                        variableName);
            }else{
                throw new RuntimeException("Unsupported flow type: " + flowType);
            }
        }
        return bpmnModel;
    }



    /**
     * 添加一个Sequence关系
     * @param process
     * @param sourceSteps
     * @param targetSteps
     */
    void addSequenceRelation(Process process, List<FlowElement> sourceSteps, List<FlowElement> targetSteps){
        FlowElement source = sourceSteps.get(sourceSteps.size() - 1);
        FlowElement target = targetSteps.get(0);
        SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), source.getId(), target.getId());
        process.addFlowElement(sequenceFlow);
    }


    void addSequenceRelation(Process process, FlowElement source, FlowElement target){
        SequenceFlow sequenceFlow = createSequenceFlow(generateTaskId(), source.getId(), target.getId());
        process.addFlowElement(sequenceFlow);
    }

    /**
     * 添加一个ExclusiveGate关系
     * @param process
     * @param sourceSteps
     * @param nextTrueSteps
     * @param nextFalseSteps
     * @param variableName
     */
    void addExclusiveGateRelation(Process process, List<FlowElement> sourceSteps, List<FlowElement> nextTrueSteps, List<FlowElement> nextFalseSteps, String variableName){
        FlowElement nextTrue = nextTrueSteps.get(0);
        FlowElement nextFalse = nextFalseSteps.get(0);
        FlowElement source = sourceSteps.get(sourceSteps.size() - 1);

        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(generateTaskId());
        process.addFlowElement(exclusiveGateway);

        process.addFlowElement(createSequenceFlow(generateTaskId(), source.getId(), exclusiveGateway.getId()));

        SequenceFlow sequenceFlowTrue = createSequenceFlow(generateTaskId(), exclusiveGateway.getId(), nextTrue.getId());
        sequenceFlowTrue.setConditionExpression("${" + variableName + " == true}");
        process.addFlowElement(sequenceFlowTrue);

        SequenceFlow sequenceFlowFalse = createSequenceFlow(generateTaskId(), exclusiveGateway.getId(), nextFalse.getId());
        sequenceFlowFalse.setConditionExpression("${" + variableName + " == false}");
        process.addFlowElement(sequenceFlowFalse);
    }

    FlowElement createStartEvent(Map<String, Object> step){
        StartEvent startEvent = new StartEvent();
        startEvent.setId(generateTaskId());
        startEvent.setName((String) step.getOrDefault("name", "START"));
        return startEvent;
    }

    FlowElement createEndEvent(Map<String, Object> step){
        EndEvent endEvent = new EndEvent();
        endEvent.setId(generateTaskId());
        endEvent.setName((String) step.getOrDefault("name", "END"));
        return endEvent;
    }


    FlowElement convertToFlowElement(Map<String, Object> step){
        String stepType = (String) step.get("step_type");
        if(stepType.equals("START")) {
            return createStartEvent(step);
        }else if(stepType.equals("END")) {
            return createEndEvent(step);
        }
        boolean isServiceTask = (boolean) step.getOrDefault("service_task", false);
        if(isServiceTask){
         return createServiceTask(step);
        }else{
         return createUserTask(step);
        }
    }

    UserTask createUserTask(Map<String, Object> step){
        UserTask userTask = new UserTask();
        userTask.setId(generateTaskId());
        userTask.setName((String) step.getOrDefault("step_type", ""));
        bindContentToUserTask(userTask, step);
        return userTask;
    }

    private List<ExtensionElement> createExtentionElement(String name, Map content){
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setName(name); // The name of your custom attribute
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
        return etsElems;
    }

    void bindContentToServiceTask(ServiceTask serviceTask, Map content) {
        List<ExtensionElement> etsElems = createExtentionElement("bind", content);
        serviceTask.setExtensionElements(Collections.singletonMap("bind", etsElems));
    }

    ServiceTask createServiceTask(Map<String, Object> step){
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(generateTaskId());
        serviceTask.setName((String) step.getOrDefault("name", ""));
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        serviceTask.setImplementation((String) step.get("implementation"));
        bindContentToServiceTask(serviceTask, step);
        return serviceTask;
    }
}
