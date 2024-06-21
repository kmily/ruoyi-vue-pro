package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.flowable.task.api.Task;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;


/**
 * 行为计划表
 */
@Component
public class ActionPlanFlow extends  BaseFlow {

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    public ActionPlanFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "ActionPlan-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/action_plan.json");
    }

    public Map<String, Object> auto_action_plan_qst(Container container, Map data, Task currentTask) {
        return data;
    }

    public void submit_action_plan_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
    }

}
