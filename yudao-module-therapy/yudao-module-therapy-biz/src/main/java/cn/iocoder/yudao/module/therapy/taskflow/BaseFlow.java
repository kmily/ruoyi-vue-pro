package cn.iocoder.yudao.module.therapy.taskflow;

import cn.hutool.core.lang.hash.Hash;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;


import javax.annotation.PreDestroy;
import java.awt.event.ContainerAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;


public abstract class BaseFlow {
    ProcessEngine processEngine;

    public BaseFlow(ProcessEngine engine){
        processEngine = engine;
    }

    public Task getCurrentTask(Container container){
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = (Task) taskService.createTaskQuery().processInstanceId(container.getProcessInstanceId()).singleResult();
        return currentTask;
    }

    protected Map getVariables(Container container){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.getVariables(container.getProcessInstanceId());
    }


     public Map fireEvent(Long dayitemInstanceId, String eventName){
         HashMap result = new HashMap();
         result.put("__step_id", "guide_language" );
         result.put("__step_name", "guide_language");
         result.put("step_type", "guide_language");
         Map stepData = new HashMap();
         stepData.put("content", "DEFAULT RESPONSE");
         result.put("step_data", stepData);
         return result;
     }

    /**
     * 前置条件是否满足
     * @param container
     * @return
     */
    public boolean prerequisiteReady(Container container){
        return true;
    }

    /**
     * 前置条件不满足
     * @param container
     * @return
     */
    public Map prerequisiteFailed(Container container){
        HashMap result = new HashMap();
        result.put("__step_id", "SYS_INFO" );
        result.put("__step_name", "SYS_INFO");
        result.put("step_type", "SYS_INFO");
        Map stepData = new HashMap();
        stepData.put("content", "您未符合相应的条件，暂时不能进行此任务");
        result.put("step_data", stepData);
        return result;
    }

    private Map exceptionResult(ServiceException e){
        HashMap result = new HashMap();
        result.put("__step_id", "SYS_INFO" );
        result.put("__step_name", "SYS_INFO");
        result.put("step_type", "SYS_INFO");
        Map stepData = new HashMap();
        stepData.put("content", e.getMessage());
        result.put("step_data", stepData);
        return result;
    }

    /**
     * 运行流程, 返回下一步的信息
     * @return
     */
    public Map run(Container container){
        HashMap result = new HashMap();
        if(container.getHistoricProcessInstance() != null){
            result.put("step_type", "END");
            Map stepData = new HashMap();
            stepData.put("content", "流程已经结束了");
            result.put("step_data", stepData);
            return result;
        }
        Task currentTask = getCurrentTask(container);
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
            Method method = this.getClass().getMethod("auto_" + (String) data.get("step_type"), Container.class, Map.class, Task.class);
            Map stepResult = (Map) method.invoke(this, container, data.get("step_data"), currentTask);
            boolean requireSubmit = (boolean) data.getOrDefault("submit", true);
            result.put("require_submit", requireSubmit);
            if (!requireSubmit || data.get("step_type").equals("guide_language")) {
                result.put("require_submit", false);
                TaskService taskService = processEngine.getTaskService();
                taskService.complete(currentTask.getId());
            }
            result.put("step_data", stepResult);
        } catch (ServiceException e){
            return exceptionResult(e);
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
    public Map auto_guide_language(Container container, Map data, Task currentTask){

        return data;
    }

    public Map auto_do_you_agree(Container container, Map data, Task currentTask){
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

    public void submit_do_you_agree(Container container, DayitemStepSubmitReqVO submitReqVO , Task currentTask){
        Map<String, Object> currentVariable = submitReqVO.getStep_data().getCurrent();
        Map bindData = getBindData(currentTask);
        String agreeKey = ((Map) bindData.get("step_data")).get("variable").toString();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable( container.getProcessInstance().getId(), agreeKey, (boolean) currentVariable.get(agreeKey));
    }


    public void userSubmit(Container container, String taskId, DayitemStepSubmitReqVO submitReqVO) {
        Task task = getCurrentTask(container);
        if(taskId.equals(task.getId())) {
            String activityId = task.getTaskDefinitionKey(); // Get the task's definition key
            BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(activityId);
            ExtensionElement ele =  userTask.getExtensionElements().get("bind").get(0);
            String content = ele.getElementText();
            Map<String, Object> data = new HashMap();
            try {
                data = new ObjectMapper().readValue(content, Map.class);
                Method method = this.getClass().getMethod("submit_" + (String) data.get("step_type"), Container.class, DayitemStepSubmitReqVO.class, Task.class);
                method.invoke(this, container, submitReqVO, task);
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
            taskService.complete(taskId);
        }else{
            throw new RuntimeException("Task id is not correct");
        }
    }

    /**
     * 创建流程实例
     * @param bmpnName
     * @return
     */
    public Container createProcessInstance(String bmpnName, Long dayItemInstanceId){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.createProcessInstanceBuilder().processDefinitionKey(bmpnName);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bmpnName, new HashMap<String, Object>());
        runtimeService.setVariable(processInstance.getId(), DAYITEM_INSTANCE_ID, dayItemInstanceId);
        Container container = new Container();
        container.setProcessInstance(processInstance);
        return container;
    }

    /**
     * 加载流程实例
     * @param processInstanceId
     */
    public Container loadProcessInstance(String processInstanceId){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Container container = new Container();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        container.setProcessInstance(processInstance);
        if(processInstance == null){
            HistoryService historyService = processEngine.getHistoryService();
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (historicProcessInstance == null) {
                throw new RuntimeException("No process instance found with id: " + processInstanceId);
            }
            container.setHistoricProcessInstance(historicProcessInstance);
        }
        return container;
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
                String nextTrue = groupRelation.get("next_true");
                String nextFalse = groupRelation.get("next_false");
                String variableName = groupRelation.get("variable");
                addExclusiveGateRelation(process, stepGroups.get(sourceGroup),
                        stepGroups.get(nextTrue), stepGroups.get(nextFalse),
                        variableName);
            }else{
                throw new RuntimeException("Unsupported flow type: " + flowType);
            }
        }
        return bpmnModel;
    }

    protected Map loadSettings( String settingsResourceFile){
        Map settings;
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream(settingsResourceFile)) {
            settings = objectMapper.readValue(inputStream, Map.class);
            // use settings
        } catch (IOException e) {
            // handle exception
            throw new RuntimeException("Failed to read settings from " + settingsResourceFile + e.getMessage());
        }
        return settings;
    }


    public String deploy(Long id, String settingsResourceFile, Map extraSettings) {
        Map settings = loadSettings(settingsResourceFile);
        List<Map> extraGuidesSettings = (List<Map>) extraSettings.getOrDefault("guides", new ArrayList());
        settings = mergeExtraGuidesSettings(settings, extraGuidesSettings);
        return deploy(settings, id);
    }

    protected String deploy(Map settings, Long id){
        BpmnModel bpmnModel = createBPMNModel(id, settings);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addBpmnModel(getProcessName(id) + ".bpmn", bpmnModel)
                .deploy();
//        System.out.println(new String(new BpmnXMLConverter().convertToXML(bpmnModel), StandardCharsets.UTF_8));
        return getProcessName(id);
    }

    private Map mergeExtraGuidesSettings(Map settings, List<Map> extraGuidesSettings){
        if(extraGuidesSettings.size() == 0){
            return settings;
        }
        ArrayList<Map> adminGuides = new ArrayList<>();
        for(Map guide: extraGuidesSettings){
            Map newGuide = new HashMap();
            newGuide.put("step_type", "guide_language");
            Map stepData = new HashMap<String, String>();
            stepData.put("content", guide.get("text"));
            newGuide.put("step_data",  stepData);
            adminGuides.add(newGuide);
        }

        Map groupNodes = (Map) settings.get("group_nodes");
        groupNodes.put("ADMIN_GUIDES", adminGuides);
        ArrayList groupRelations = (ArrayList) settings.get("group_relations");
        HashMap firstRel = new HashMap<>();
        firstRel.put("source", "start");
        firstRel.put("target", "ADMIN_GUIDES");

        for (int i = 0; i < groupRelations.size(); i++) {
            Map rel = (Map) groupRelations.get(i);
            if(rel.get("source").equals("start")){
                rel.put("source", "ADMIN_GUIDES");
            }
        }
        groupRelations.add(0, firstRel);

        // set
        settings.put("group_relations", groupRelations);
        settings.put("group_nodes", groupNodes);
//        System.out.println("merged work flow settings, you can comment this if in production: \n" + settings);
        return settings;
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

    EndEvent createEndEvent(Map<String, Object> step){
        EndEvent endEvent = new EndEvent();
        endEvent.setId(generateTaskId());
        ExtensionAttribute ets = new ExtensionAttribute();
        ets.setName("reason");
        ets.setValue("completed");
        endEvent.addAttribute(ets);
        endEvent.setName((String) step.getOrDefault("name", "END"));
        return endEvent;
    }

    public abstract void onFlowEnd(DelegateExecution execution);


    FlowableListener getFlowableListener(){
        FlowableListener listener = new FlowableListener();
        listener.setEvent("end");
        listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        listener.setImplementation("#{taskFlowService}");
        return listener;
    }


    FlowElement convertToFlowElement(Map<String, Object> step){
        String stepType = (String) step.get("step_type");
        if(stepType.equals("START")) {
            return createStartEvent(step);
        }else if(stepType.equals("END")) {
            EndEvent endEvent = createEndEvent(step);
            List<FlowableListener> listeners = new ArrayList<>();
            listeners.add(getFlowableListener());
            endEvent.setExecutionListeners(listeners);
            return endEvent;
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
        if(step.getOrDefault("delegateExpression", "").equals("")){
            // Delegate expression is empty, use implementation
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
            serviceTask.setImplementation((String) step.get("implementation"));
        }else{
            serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            serviceTask.setImplementation((String) step.get("delegateExpression"));
        }
        bindContentToServiceTask(serviceTask, step);
        return serviceTask;
    }


}
