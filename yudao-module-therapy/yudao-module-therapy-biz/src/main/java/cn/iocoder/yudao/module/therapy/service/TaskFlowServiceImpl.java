package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import cn.iocoder.yudao.module.therapy.taskflow.Engine;
import cn.iocoder.yudao.module.therapy.taskflow.GoalAndMotivationFlow;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Component("taskFlowService")
public class TaskFlowServiceImpl implements TaskFlowService, ExecutionListener {

    @Resource
    TreatmentUserProgressMapper treatmentUserProgressMapper;

    @Resource
    TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    GoalAndMotivationFlow goalAndMotivationFlow;

    @Resource
    private Engine engine;

    public BaseFlow getTaskFlow(Long userId, Long treatmentInstanceId, Long dayItemInstanceId){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        switch (flowDayitemDO.getItemType()){
            case "problem_goal_motive":
                if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
                    // need to create a new task instance
                    if(flowDayitemDO.getTaskFlowId() == null){
                        throw new RuntimeException("task flow id is null");
                    }
                    // create a new task instance
                    String bmpnName = goalAndMotivationFlow.getProcessName(flowDayitemDO.getId());
                    String taskInstanceId = goalAndMotivationFlow.createProcessInstance(bmpnName, dayItemInstanceId);
                    dayitemInstanceDO.setTaskInstanceId(taskInstanceId);
                    treatmentDayitemInstanceMapper.updateById(dayitemInstanceDO);
                }else{
                    // get the task instance
                    goalAndMotivationFlow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
                }
                return goalAndMotivationFlow;
            default:
                return null;
        }
    }

    @Override
    public void createBpmnModel(Long flowDayitemId){
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(flowDayitemId);
        switch (flowDayitemDO.getItemType()){
            case "problem_goal_motive":
                GoalAndMotivationFlow goalAndMotivationFlow = new GoalAndMotivationFlow(engine.getEngine());
                String taskFlowId = goalAndMotivationFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(taskFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            default:
                break;
        }
    }

    @Override
    public Map getNext(Long userId, Long treatmentInstanceId, Long dayItemInstanceId){
        BaseFlow taskFlow = getTaskFlow(userId, treatmentInstanceId, dayItemInstanceId);
        return taskFlow.run();
    }

    @Override
    public void userSubmit(BaseFlow taskFlow, Long dayitem_instance_id, String taskId, Map<String, Object> variables){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitem_instance_id);
        taskFlow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        taskFlow.userSubmit(taskId, variables);
    }

    @Override
    public void notify(DelegateExecution execution) {
        Map<String, Object> variables =  execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get("dayItemInstanceId");
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        Long userId = treatmentDayitemInstanceDO.getUserId();
        Long treatmentInstanceId = treatmentDayitemInstanceDO.getFlowInstanceId();
        BaseFlow taskFlow = getTaskFlow(userId, treatmentInstanceId, dayItemInstanceId);
        taskFlow.loadProcessInstance(treatmentDayitemInstanceDO.getTaskInstanceId());
        taskFlow.onFlowEnd(execution);
    }


//    @Override
//    public void execute(DelegateExecution execution) {
//        Map<String, Object> variables =  execution.getVariables();
//        Long dayItemInstanceId = (Long) variables.get("dayItemInstanceId");
//        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
//        Long userId = treatmentDayitemInstanceDO.getUserId();
//        Long treatmentInstanceId = treatmentDayitemInstanceDO.getFlowInstanceId();
//        BaseFlow taskFlow = getTaskFlow(userId, treatmentInstanceId, dayItemInstanceId);
//        taskFlow.loadProcessInstance(treatmentDayitemInstanceDO.getTaskInstanceId());
//        taskFlow.onFlowEnd(execution);
//    }
}
