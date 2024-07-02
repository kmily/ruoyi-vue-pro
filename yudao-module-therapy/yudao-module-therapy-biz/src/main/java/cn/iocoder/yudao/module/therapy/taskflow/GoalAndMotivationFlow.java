package cn.iocoder.yudao.module.therapy.taskflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.DayitemInstanceService;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import org.flowable.engine.*;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.TREATMENT_DAYITEM_STEP_PARAMS_ERROR;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;


@Component
@Scope("prototype")
public class GoalAndMotivationFlow extends BaseFlow{
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    @Resource
    DayitemInstanceService dayitemInstanceService;

    @Resource
    private TreatmentService treatmentService;

    public GoalAndMotivationFlow(ProcessEngine engine) {
        super(engine);
    }

    public String deploy(Long id, Map<String, Object> settings) {
        return super.deploy(id, "/goal_and_motivation_flow.json", settings);
//        // read settings from resources/goal_and_motivation_settings.json
//        ObjectMapper objectMapper = new ObjectMapper();
//        try (InputStream inputStream = getClass().getResourceAsStream("/goal_and_motivation_flow.json")) {
//            settings = objectMapper.readValue(inputStream, Map.class);
//            // use settings
//        } catch (IOException e) {
//            // handle exception
//            throw new RuntimeException("Failed to read settings from resources/goal_and_motivation_settings.json");
//        }
//        BpmnModel bpmnModel = createBPMNModel(id, settings);
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.createDeployment()
//                .addBpmnModel("GoalAndMotivationFlow-" + String.valueOf(id) + ".bpmn", bpmnModel)
//                .deploy();
//        System.out.println(new String(new BpmnXMLConverter().convertToXML(bpmnModel), StandardCharsets.UTF_8));
//        return getProcessName(id);
    }


    @Override
    public String getProcessName(Long id) {
        return "GOAL_AND_MOTIVATION-"  + String.valueOf(id);
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        // TODO if user don't agree should cancel all the treatment
        treatmentService.finishDayItemInstance(dayItemInstanceId);
        if (!(boolean) variables.get("try_treatment_confirm_result")){
            // cancel all the treatment
            TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
            treatmentService.cancelTreatmentInstance(dayitemInstanceDO.getFlowInstanceId());
        }
    }

    /**
     * 烦恼问题描述
     * @param data
     * @param currentTask
     * @return
     */
    public Map<String, Object> auto_primary_troubles_qst(Container container, Map data, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Long instance_id = (Long) runtimeService.getVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID);
        if(instance_id == null) {
            instance_id = surveyService.initSurveyAnswer(SurveyType.PROBLEM_GOAL_MOTIVE.getCode(), SURVEY_SOURCE_TYPE);
            runtimeService.setVariable(container.getProcessInstanceId(), SURVEY_INSTANCE_ID, instance_id);
        }
        data.put("instance_id", instance_id);
        return data;
    }


    /**
     * 提交问题描述
     * @param submitReqVO
     * @param currentTask
     */
    public void submit_primary_troubles_qst(Container container,DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        // set troubles text, for later llm use
        Map stepVariables = submitReqVO.getStep_data().getCurrent();
        if (stepVariables == null){
            return;
        }

        List<String> troubles;
        troubles =  (List<String>) stepVariables.getOrDefault("troubles", new ArrayList<>());
        if(troubles.size() > 0){
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.setVariable(currentTask.getProcessInstanceId(), "troubles", troubles);
            // submit survey data
            SubmitSurveyReqVO survey = submitReqVO.getStep_data().getSurvey();
            if(survey != null){
                surveyService.submitSurveyForFlow(survey);
            }
        }
    }

    /**
     * 设定目标
     * @param data
     * @param currentTask
     * @return
     */
    public  Map<String, Object> auto_set_goal_qst(Container container, Map data, Task currentTask){
        Map variables = getVariables(container);
        data.put("instance_id", (Long) variables.get(SURVEY_INSTANCE_ID));
        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get(SURVEY_INSTANCE_ID));
        data.put("instance_data", instanceData);
        return data;
    }

    protected Map getVariables(Container container){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.getVariables(container.getProcessInstanceId());
    }


    /**
     * 提交设定目标
     * @param submitReqVO
     * @param currentTask
     */
    public void submit_set_goal_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    /**
     * 我的行动
     * @param data
     * @param currentTask
     * @return
     */
    public Map<String, Object> auto_my_actions_qst(Container container,Map data, Task currentTask){
        Map variables = getVariables(container);
        data.put("instance_id", variables.get(SURVEY_INSTANCE_ID));

        List<AnswerDetailDO> instanceData = surveyService.getAnswerDetailByAnswerId((Long) variables.get(SURVEY_INSTANCE_ID));
        data.put("instance_data", instanceData);
        return data;
    }

    public void submit_my_actions_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }

    /**
     * 问题分类选择
     * @param data
     * @param currentTask
     * @return
     */
    public Map<String, Object> auto_trouble_category_qst(Container container,Map data, Task currentTask){
        Map variables = getVariables(container);
        data.put("instance_id", variables.get(SURVEY_INSTANCE_ID));
        return data;
    }

    /**
     * 提交问题分类选择
     * @param submitReqVO
     * @param currentTask
     */
    public void submit_trouble_category_qst(Container container,DayitemStepSubmitReqVO submitReqVO, Task currentTask){
        Map variables = getVariables(container);
        Long dayitemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        List<String> troublesCategory = (List<String>) submitReqVO.getStep_data().getStore().get(USER_TROUBLE_CATEGORIES);
        if(troublesCategory == null || troublesCategory.size() == 0 ){
            throw exception(TREATMENT_DAYITEM_STEP_PARAMS_ERROR);
        }
        dayitemInstanceService.updateDayitemInstanceExtAttr(dayitemInstanceId, USER_TROUBLE_CATEGORIES, troublesCategory);
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
