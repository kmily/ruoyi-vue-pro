package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.flowable.task.api.Task;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;

@Component
public class TwelveMindDistort extends BaseFlow{

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    public TwelveMindDistort(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "TWELVE_MIND_DISTORT-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/twelve_mind_distort.json");
    }

    public Map<String, Object> auto_twelve_problems_qst(Container container, Map data, Task currentTask) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.TWELVE_MIND_DISTORT.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_twelve_problems_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public Map<String, Object> auto_strategy_card_qst(Container container, Map data, Task currentTask) {
        String STRATEGY_CARD_SURVEY_ID = "STRATEGY_CARD_SURVEY_ID";
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), STRATEGY_CARD_SURVEY_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.STRATEGY_CARD.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), STRATEGY_CARD_SURVEY_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_strategy_card_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
