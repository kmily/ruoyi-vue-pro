package cn.iocoder.yudao.module.therapy.taskflow;
/**
 * 认知重建流程
 */

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import cn.iocoder.boot.module.therapy.enums.SurveyType;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.flowable.task.api.Task;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;


@Component
public class CognitionReconstructFlow extends BaseFlow{
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    public CognitionReconstructFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "COGNIZE_REBUILD-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }



    public Map<String, Object> auto_recall_qst(Container container,Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.COGNIZE_REBUILD.getCode(), SURVEY_SOURCE_TYPE); //TODO
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/cognition_reconstruct.json");
    }

    public void submit_recall_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    public void submit_reconstruct_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }


    public Map<String, Object> auto_reconstruct_qst(Container container,Map data, Task currentTask){
        Map variables = getVariables(container);
        Long instance_id = surveyService.initSurveyAnswer(SurveyType.COGNIZE_REBUILD.getCode(), SURVEY_SOURCE_TYPE); //TODO
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get(SURVEY_INSTANCE_ID));
        data.put("instance_data", instanceData);
        data.put("instance_id", instance_id);
        return data;
    }

    public Map<String, Object> auto_report(Container container,Map data, Task currentTask){
        return data;
    }

}
