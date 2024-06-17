package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.task.api.Task;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;

/**
 * 量表流程  - 初步评估
 */
@Component
public class ScaleFlow extends BaseFlow{
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;


    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/scale_flow.json");
    }


    public ScaleFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "Liangbiao-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_survey_questions(Container container, Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String surveyIdKey = "survey_instance_id+" + data.get("code");
        Object survey_instance_id = runtimeService.getVariable(container.getProcessInstanceId(), surveyIdKey);
        Long instance_id;
        String surveyCode = (String) data.get("code");

        if(survey_instance_id  == null){
            int sourceTypeCustomize = 2;
            instance_id =  surveyService.initSurveyAnswer(surveyCode, sourceTypeCustomize);
            runtimeService.setVariable(container.getProcessInstanceId(), surveyIdKey, instance_id);
        }else{
            instance_id = (Long) survey_instance_id;
        }
        Long surveyId = surveyService.getSurveyIdByCode(surveyCode);
        List<QuestionDO> questionDOS = surveyService.getQuestionBySurveyId(surveyId);
        List<AnswerDetailDO> instanceData =  surveyService.getAnswerDetailByAnswerId(instance_id);
        data.put("instance_id", instance_id);
        data.put("questions", questionDOS);
        data.put("instance_data", instanceData);;
        return data;
    }

    private Long submitSurveyData(Container container,Map variables){
        Map survey = (Map) variables.get("__survey");
        Map surveyData = (Map) survey.get("data");
        SubmitSurveyReqVO submitSurveyReqVO = new ObjectMapper().convertValue(surveyData, SubmitSurveyReqVO.class);
        return surveyService.submitSurveyForFlow(submitSurveyReqVO);
    }

    protected Map getVariables(Container container){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.getVariables(container.getProcessInstanceId());
    }

    public void submit_survey_questions(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        // submit survey data
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public Map<String, Object> auto_survey_report(Container container,Map data, Task currentTask){
        Map variables = getVariables(container);
        data.put("instance_id", (Long) variables.get(SURVEY_INSTANCE_ID));
        return data;
    }

    public Map<String, Object> auto_review_result(Container container, Map data, Task currentTask){
        Map variables = getVariables(container);
        data.put("instance_id", (Long) variables.get(SURVEY_INSTANCE_ID));
        return data;
    }

}
