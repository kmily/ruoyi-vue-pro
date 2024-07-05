package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;

@Component
public class CaseStudyFlow extends BaseFlow{

    @Resource
    TreatmentStatisticsDataService treatmentStatisticsDataService;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    @Resource
    private TreatmentService treatmentService;


    public CaseStudyFlow(ProcessEngine engine) {
        super(engine);
    }


    @Override
    public String getProcessName(Long id) {
        return "CASE_STUDY_FLOW-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentService.finishDayItemInstance(dayItemInstanceId);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/case_study_flow.json", settings);
    }


    /**
     * 前置条件是否满足
     * @param container
     * @return
     */
    public boolean prerequisiteReady(Container container){
        Long dayItemInstanceId =(Long) getVariables(container).get(DAYITEM_INSTANCE_ID);;
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        List<String> troubleTags = treatmentStatisticsDataService.queryUserTroubles(dayitemInstanceDO.getUserId());
        if(troubleTags.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 前置条件不满足
     * @param container
     * @return
     */
    public Map prerequisiteFailed(Container container){
        HashMap result = new HashMap();
        result.put("__step_id", "SYS_INFO" );
        result.put("__step_name", "SYS_INFO");
        result.put("step_type", "SYS_INFO");
        Map stepData = new HashMap();
        stepData.put("content", "您还没有完成目标与动机中的问题类别设定，请完成后继续！");
        result.put("step_data", stepData);
        return result;
    }



    public Map auto_mood_diary_qst(Container container,Map data, Task currentTask){
//        Long dayItemInstanceId =(Long) getVariables(container).get(DAYITEM_INSTANCE_ID);;
//        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
//        List<String> troubleTags = treatmentStatisticsDataService.queryUserTroubles(dayitemInstanceDO.getUserId());
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.CASE_STUDY.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }



    public void submit_mood_diary_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
