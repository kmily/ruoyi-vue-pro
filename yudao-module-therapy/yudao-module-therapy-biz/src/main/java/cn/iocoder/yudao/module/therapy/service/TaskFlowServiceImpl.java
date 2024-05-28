package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import cn.iocoder.yudao.module.therapy.taskflow.GoalAndMotivationFlow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskFlowServiceImpl implements TaskFlowService {

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

    public BaseFlow getTaskFlow(Long userId, Long treatmentInstanceId, Long dayItemInstanceId){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        switch (flowDayitemDO.getItemType()){
            case "goal_and_motivation":
                GoalAndMotivationFlow goalAndMotivationFlow = new GoalAndMotivationFlow();
                if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
                    // need to create a new task instance
                    if(flowDayitemDO.getTaskFlowId() == null){
                        throw new RuntimeException("task flow id is null");
                    }
                    // create a new task instance
                    String bmpnName = flowDayitemDO.getTaskFlowId();
                    String taskInstanceId = goalAndMotivationFlow.createProcessInstance(bmpnName);
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
            case "goal_and_motivation":
                GoalAndMotivationFlow goalAndMotivationFlow = new GoalAndMotivationFlow();
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
    public void userSubmit(Long dayitem_instance_id, String taskId, Map<String, Object> variables){
        GoalAndMotivationFlow goalAndMotivationFlow = new GoalAndMotivationFlow();
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitem_instance_id);
        goalAndMotivationFlow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        goalAndMotivationFlow.userSubmit(taskId, variables);
    }
}
