package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.TREATMENT_NO_CASE_STUDY_FOUND;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;

@Component
@Slf4j
public class CaseStudyFlow extends BaseFlow{

    @Resource
    TreatmentStatisticsDataService treatmentStatisticsDataService;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;
    @Resource
    private TreatmentService treatmentService;

    @Resource
    private TreatmentSurveyMapper treatmentSurveyMapper;


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
        List<String> troubleTags = treatmentStatisticsDataService.queryUserTroubles(dayitemInstanceDO);
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


    private List<TreatmentSurveyDO> querySurveys(Container container, String tag){
        List<TreatmentSurveyDO> surveyDOS = surveyService.listByTag(tag,SurveyType.CASE_STUDY.getType());
        if(surveyDOS.isEmpty()){
            log.error("[ERROR] no Case Study found for tag: " + tag);
            throw exception(TREATMENT_NO_CASE_STUDY_FOUND);
        }
        return surveyDOS;
    }

    private String queryTag(Container container){
        Long dayItemInstanceId =(Long) getVariables(container).get(DAYITEM_INSTANCE_ID);;
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        List<String> troubleTags = treatmentStatisticsDataService.queryUserTroubles(dayitemInstanceDO);
        int randIndex = new Random().nextInt(troubleTags.size());
        String tag = troubleTags.get(randIndex);
        return tag;
    }


    public Map auto_survey_qst(Container container,Map data, Task currentTask){
        String tag = queryTag(container);
        tag = "焦虑";
        List<TreatmentSurveyDO> surveyDOS = querySurveys(container, tag);
        Map variables = getVariables(container);
        Long survey_id = (Long) variables.get("survey_id");
        TreatmentSurveyDO surveyDO;
        RuntimeService runtimeService = processEngine.getRuntimeService();
        if(survey_id == null || survey_id == 0L){
            Random random = new Random();
            int rInt = random.nextInt();
            int index = rInt % surveyDOS.size();
            surveyDO = surveyDOS.get(index);
            runtimeService.setVariable(container.getProcessInstanceId(), "survey_id", surveyDO.getId());
        }else{
            surveyDO = treatmentSurveyMapper.selectById(survey_id);
        }
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(surveyDO.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        List<QuestionDO> questionDOS = surveyService.getQuestionBySurveyId(surveyDO.getId());
        data.put("survey_id", surveyDO.getId());
        data.put("instance_id", instance_id);
        data.put("questions", questionDOS);
        data.put("description", surveyDO.getDescription());
        data.put("trouble_tag", tag);
        return data;
    }

    public void submit_survey_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
