package cn.iocoder.yudao.module.therapy.taskflow;


import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.flowable.task.api.Task;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_INSTANCE_ID;

@Component
public class StrategyPracticeFlow extends BaseFlow {

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    @Resource
    TreatmentSurveyMapper treatmentSurveyMapper;

    public StrategyPracticeFlow(ProcessEngine engine) {
        super(engine);
    }


    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/strategy_practice.json");
    }

    @Override
    public String getProcessName(Long id) {
        return "StrategyPractice-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_strategy_practice_survey(Container container, Map data, Task currentTask){
        // # TODO Bug
        String tag = "不开心";
        List<TreatmentSurveyDO> surveyDOS = surveyService.listByTag(tag);
        if(surveyDOS.isEmpty()){
            throw new RuntimeException("No survey found for tag: " + tag);
        }
        Map variables = getVariables(container);
        Long survey_id = (Long) variables.get("survey_id");

        TreatmentSurveyDO surveyDO;
        if(survey_id == null){
            Random random = new Random();
            int rInt = random.nextInt();
            int index = rInt % surveyDOS.size();
            surveyDO = surveyDOS.get(index);
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.setVariable(container.getProcessInstanceId(), "survey_id", surveyDO.getId());

        }else{
            surveyDO = treatmentSurveyMapper.selectById(survey_id);
        }

        List<QuestionDO> questionDOS = surveyService.getQuestionBySurveyId(surveyDO.getId());
        data.put("survey_id", surveyDO.getId());
        data.put("questions", questionDOS);
        data.put("description", surveyDO.getDescription());
        data.put("trouble_tag", tag);

        return data;
    }

    public Map<String, Object> auto_strategy_card(Container container, Map data, Task currentTask) {
        Map variables = getVariables(container);
        Long survey_id = (Long) variables.get("survey_id");
        TreatmentSurveyDO surveyDO = treatmentSurveyMapper.selectById(survey_id);
        TreatmentSurveyDO cardSurvey = treatmentSurveyMapper.selectById(surveyDO.getRelSurveyList().get(0));
        int SOURCE_MAIN = 2;
        Long instance_id = -1L;
        if(variables.get(SURVEY_INSTANCE_ID) != null) {
            instance_id = (Long) variables.get(SURVEY_INSTANCE_ID);
        }
        if( instance_id < 0){
            instance_id = surveyService.initSurveyAnswer(cardSurvey.getCode(), SOURCE_MAIN);
            variables.put(SURVEY_INSTANCE_ID, instance_id);
        }
        List<AnswerDetailDO> instanceData =  surveyService.getAnswerDetailByAnswerId(instance_id);
        List<QuestionDO> questionDOS = surveyService.getQuestionBySurveyId(cardSurvey.getId());

        data.put("instance_id", instance_id);
        data.put("instance_data", instanceData);
        data.put("description", cardSurvey.getDescription());
        data.put("questions", questionDOS);
        return data;
    }

    public void submit_strategy_card(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
