package cn.iocoder.yudao.module.therapy.taskflow;
/**
 * 自动化思维识别
 */

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;

@Component
public class AutoMindsetRecognize extends BaseFlow {

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    public AutoMindsetRecognize(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "THOUGHTS_RECOGNIZE-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/thoughts_recognize.json");
    }

    public Map<String, Object> auto_recognize_methods_qst(Container container, Map data, Task currentTask) {
        return data;
    }

    public void submit_recognize_methods_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        boolean is_llm = (boolean) submitReqVO.getStep_data().getCurrent().get("is_llm");
        runtimeService.setVariable(container.getProcessInstanceId(), "is_llm", is_llm);
    }

    public Map<String, Object> auto_llm_report(Container container, Map data, Task currentTask) {
        return data;
    }

    public Map<String, Object> auto_thoughts_describe_qst(Container container, Map data, Task currentTask) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.AUTO_THOUGHT_RECOGNITION.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public void submit_thoughts_describe_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public Map<String, Object> auto_report(Container container, Map data, Task currentTask) {
        Map variables = getVariables(container);
        Long instance_id = (Long) variables.get(SURVEY_INSTANCE_ID);
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get(SURVEY_INSTANCE_ID));
        data.put("instance_data", instanceData);
        data.put("instance_id", instance_id);
        return data;
    }


}
