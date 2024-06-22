package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.TREATMENT_REQUIRE_GOAL_AND_MOTIVATION;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;

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

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/goal_progress.json");
    }

    public Map<String, Object> auto_goal_progress_qst(Container container, Map data, Task currentTask) {
        Map variables = getVariables(container);
        Long dayitemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitemInstanceId);
        SubmitSurveyReqVO surveyReqVO = surveyService.getGoalMotive(dayitemInstanceDO.getUserId());
        if(surveyReqVO == null){
            throw exception(TREATMENT_REQUIRE_GOAL_AND_MOTIVATION);
        }
        Long instance_id = surveyReqVO.getId();
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId(instance_id);
        data.put("instance_id", instance_id);
        data.put("instance_data", instanceData);
        return data;
    }

    public void submit_goal_progress_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
