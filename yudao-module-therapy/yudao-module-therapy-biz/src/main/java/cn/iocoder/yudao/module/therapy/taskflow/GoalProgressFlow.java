package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;

@Component
public class GoalProgressFlow extends BaseFlow{

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    @Resource
    TreatmentStatisticsDataService treatmentStatisticsDataService;


    public GoalProgressFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "GOAL_PROGRESS-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_goal_progress_qst(Container container, Map data, Task currentTask) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.MOOD_RECOGNITION.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }

        Map variables = getVariables(container);
        Long dayitemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitemInstanceId);
        List<String> goals = treatmentStatisticsDataService.queryUserGoals(dayitemInstanceDO.getUserId());

        data.put("instance_id", instance_id);
        data.put("goals", goals);
        return data;
    }

    public void submit_goal_progress_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
