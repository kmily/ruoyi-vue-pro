package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import cn.iocoder.yudao.module.therapy.taskflow.Container;
import cn.iocoder.yudao.module.therapy.taskflow.Engine;
import cn.iocoder.yudao.module.therapy.taskflow.GoalAndMotivationFlow;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.event.ContainerAdapter;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;

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

    private Container getContainer(BaseFlow flow, TreatmentDayitemInstanceDO dayitemInstanceDO, TreatmentFlowDayitemDO flowDayitemDO){
        Container container =  new Container();
        if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
            // need to create a new task instance
            if(flowDayitemDO.getTaskFlowId() == null){
                throw new RuntimeException("task flow id is null");
            }
            // create a new task instance
            String bmpnName = flow.getProcessName(flowDayitemDO.getId());
            container = flow.createProcessInstance(bmpnName, dayitemInstanceDO.getId());
            return container;
        }else{
            // get the task instance
            return flow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        }
    }

    @Override
    public BaseFlow getTaskFlow(TreatmentFlowDayitemDO flowDayitemDO){
        switch (flowDayitemDO.getItemType()){
            case "problem_goal_motive":
                return goalAndMotivationFlow;
            default:
                return null;
        }
    }

    @Override
    public BaseFlow getTaskFlow(Long userId,  Long dayItemInstanceId){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        return getTaskFlow(flowDayitemDO);
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
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        BaseFlow taskFlow = getTaskFlow(flowDayitemDO);
        Container container = getContainer(taskFlow, dayitemInstanceDO, flowDayitemDO);
        if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
            dayitemInstanceDO.setTaskInstanceId(container.getProcessInstanceId());
            treatmentDayitemInstanceMapper.updateById(dayitemInstanceDO);
        }
        return taskFlow.run(container);
    }

    @Override
    public void userSubmit(BaseFlow taskFlow, Long dayitem_instance_id, String taskId, DayitemStepSubmitReqVO submitReqVO){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitem_instance_id);
        Container container = taskFlow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        taskFlow.userSubmit(container, taskId, submitReqVO);
    }

    @Override
    public void notify(DelegateExecution execution) {
        Map<String, Object> variables =  execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        Long userId = treatmentDayitemInstanceDO.getUserId();
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        BaseFlow taskFlow = getTaskFlow(flowDayitemDO);
        taskFlow.loadProcessInstance(treatmentDayitemInstanceDO.getTaskInstanceId());
        taskFlow.onFlowEnd(execution);
    }

}
