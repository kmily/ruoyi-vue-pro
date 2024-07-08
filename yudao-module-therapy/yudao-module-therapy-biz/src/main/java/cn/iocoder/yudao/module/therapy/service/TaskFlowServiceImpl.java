package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.taskflow.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.boot.module.therapy.enums.TaskType.*;
import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;

@Service
@Component("taskFlowService")
public class TaskFlowServiceImpl implements TaskFlowService, ExecutionListener {

    @Resource
    TreatmentUserProgressMapper treatmentUserProgressMapper;

    @Resource
    TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    GoalAndMotivationFlow goalAndMotivationFlow;

    @Resource
    MoodRecognizeNamedFlow moodRecognizeNamedFlow;

    @Resource
    AutoMindsetRecognizeFlow autoMindsetRecognizeFlow;

    @Resource
    ScaleFlow scaleFlow;

    @Resource
    private Engine engine;

    @Resource
    TwelveMindDistortFlow twelveMindDistort;

    @Resource
    CognitionReconstructFlow cognitionReconstructFlow;

    @Resource
    HappyActiveFlow happyActiveFlow;

    @Resource
    private MoodScoreFlow moodScoreFlow;

    @Resource
    private ActionPlanFlow actionPlanFlow;

    @Resource
    private GoalProgressFlow goalProgressFlow;

    @Resource
    private StrategyPracticeFlow strategyPracticeFlow;

    @Resource
    private CaseStudyFlow caseStudyFlow;

    @Resource
    private ScheduleChartFlow scheduleChartFlow;

    @Resource
    private TreatmentCalendarFlow treatmentCalendarFlow;

    @Resource
    private RelaxExerciseFlow relaxExerciseFlow;

    @Resource
    VideoTreatmentFlow videoTreatmentFlow;

    private Container getContainer(BaseFlow flow, TreatmentDayitemInstanceDO dayitemInstanceDO, TreatmentFlowDayitemDO flowDayitemDO){
        Container container =  new Container();
        if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
            // need to create a new task instance
            if(flowDayitemDO.getTaskFlowId() == null){
                throw new RuntimeException("task flow id is null");
            }
            // create a new task instance
            String bmpnName = flow.getProcessName(flowDayitemDO.getId());
            container = flow.createProcessInstance(bmpnName, dayitemInstanceDO.getId());
            return container;
        }else{
            // get the task instance
            return flow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        }
    }

    @Override
    public BaseFlow getTaskFlow(TreatmentFlowDayitemDO flowDayitemDO){
        String itemType = flowDayitemDO.getItemType();
        if (PROBLEM_GOAL_MOTIVE.getCode().equals(itemType)) { // 目标与动机
            return goalAndMotivationFlow;
        } else if (SCALE.getCode().equals(itemType)) { // 初步评估
            return scaleFlow;
        } else if (CURRENT_MOOD_SCORE.getCode().equals(itemType)) { // 情绪评分
            return moodScoreFlow;
        } else if (MOOD_RECOGNIZE_NAMED.getCode().equals(itemType)) { // 情绪识别
            return moodRecognizeNamedFlow;
        } else if (AUTO_MINDSET_RECOGNIZE.getCode().equals(itemType)) { //自动化思维识别
            return autoMindsetRecognizeFlow;
        } else if (TWELVE_MIND_DISTORT.getCode().equals(itemType)) { //12中心理歪曲
            return twelveMindDistort;
        } else if (COGNIZE_REESTABLISH.getCode().equals(itemType)) { //认知重建
            return cognitionReconstructFlow;
        } else if (HAPPLY_EXERCISE_LIST.getCode().equals(itemType)) { //愉悦活动清单
            return happyActiveFlow;
        } else if (BEHAVIOUR_EXERCISE_PLAN.getCode().equals(itemType)) { //行为活动计划
            return actionPlanFlow;
        } else if (GOAL_PROGRESS.getCode().equals(itemType)) { // 目标进展
            return goalProgressFlow;
        } else if (STRATEGY_PRACTICE.getCode().equals(itemType)) { //对策游戏
            return strategyPracticeFlow;
        } else if (CASE_STUDY.getCode().equals(itemType)) {
            return caseStudyFlow;
        } else if (VIDEO.getCode().equals(itemType)) {
            return videoTreatmentFlow;
        } else if (SCHEDULE.getCode().equals(itemType)) {
            return scheduleChartFlow;
        } else if (THERAPY_CALENDAR.getCode().equals(itemType)) {
            return treatmentCalendarFlow;
        } else if (RELAX_EXERCISE.getCode().equals(itemType)){
            return relaxExerciseFlow;
        }
        else {
            throw new RuntimeException("not supported task flow");
        }

    }

    @Override
    public BaseFlow getTaskFlow(Long userId,  Long dayItemInstanceId){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        return getTaskFlow(flowDayitemDO);
    }

    @Override
    public void updateFlowFromDayitem(TreatmentFlowDayitemDO flowDayitemDO, String type){
        if(flowDayitemDO.getItemType().equals(TaskType.GUIDE_LANGUAGE.getCode())){
            return;
        }
        createBpmnModel(flowDayitemDO.getId()); // 更新工作流
    }


    @Override
    public void createBpmnModel(Long flowDayitemId){
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(flowDayitemId);
        String itemType = flowDayitemDO.getItemType();
        if (VIDEO.getCode().equals(itemType)) {
            VideoTreatmentFlow videoTreatmentFlow = new VideoTreatmentFlow(engine.getEngine());
            String taskFlowId = videoTreatmentFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
            flowDayitemDO.setTaskFlowId(taskFlowId);
            treatmentFlowDayitemMapper.updateById(flowDayitemDO);
            return;
        }else if(CASE_STUDY.getCode().equals(itemType)){
            CaseStudyFlow caseStudyFlow = new CaseStudyFlow(engine.getEngine());
            String taskFlowId = caseStudyFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
            flowDayitemDO.setTaskFlowId(taskFlowId);
            treatmentFlowDayitemMapper.updateById(flowDayitemDO);
            return;
        }else if(SCHEDULE.getCode().equals(itemType)) {
            ScheduleChartFlow scheduleChartFlow = new ScheduleChartFlow(engine.getEngine());
            String taskFlowId = scheduleChartFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
            flowDayitemDO.setTaskFlowId(taskFlowId);
            treatmentFlowDayitemMapper.updateById(flowDayitemDO);
            return;
        }else if(THERAPY_CALENDAR.getCode().equals(itemType)) {
            TreatmentCalendarFlow treatmentCalendarFlow = new TreatmentCalendarFlow(engine.getEngine());
            String taskFlowId = treatmentCalendarFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
            flowDayitemDO.setTaskFlowId(taskFlowId);
            treatmentFlowDayitemMapper.updateById(flowDayitemDO);
            return;
        }else if (RELAX_EXERCISE.getCode().equals(itemType)) {
            RelaxExerciseFlow relaxExerciseFlow = new RelaxExerciseFlow(engine.getEngine());
            String taskFlowId = relaxExerciseFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
            flowDayitemDO.setTaskFlowId(taskFlowId);
            treatmentFlowDayitemMapper.updateById(flowDayitemDO);
            return;
        }

            switch (flowDayitemDO.getItemType()){
            case "problem_goal_motive":  // 目标与动机
                GoalAndMotivationFlow goalAndMotivationFlow = new GoalAndMotivationFlow(engine.getEngine());
                String taskFlowId = goalAndMotivationFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(taskFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "scale": // 初步评估
                ScaleFlow scaleFlow = new ScaleFlow(engine.getEngine());
                String scaleFlowId = scaleFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(scaleFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "current_mood_score": // 情绪评分
                MoodScoreFlow moodScoreFlow = new MoodScoreFlow(engine.getEngine());
                String moodScoreFlowId = moodScoreFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(moodScoreFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "mood_recognize_named": // 情绪识别
                MoodRecognizeNamedFlow moodRecognizeNamedFlow = new MoodRecognizeNamedFlow(engine.getEngine());
                String moodRecognizeNamedFlowId = moodRecognizeNamedFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(moodRecognizeNamedFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "auto_mindset_recognize": // 自动化思维识别
                AutoMindsetRecognizeFlow autoMindsetRecognizeFlow = new AutoMindsetRecognizeFlow(engine.getEngine());
                String autoMindsetRecognizeId = autoMindsetRecognizeFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(autoMindsetRecognizeId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "twelve_mind_distort":
                TwelveMindDistortFlow twelveMindDistort = new TwelveMindDistortFlow(engine.getEngine());
                String twelveMindDistortId = twelveMindDistort.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(twelveMindDistortId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "cognize_reestablish":
                CognitionReconstructFlow cognitionReconstructFlow = new CognitionReconstructFlow(engine.getEngine());
                String cognizeReestablishId = cognitionReconstructFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(cognizeReestablishId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case  "happly_exercise_list":
                HappyActiveFlow happyActiveFlow = new HappyActiveFlow(engine.getEngine());
                String happyActiveFlowId = happyActiveFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(happyActiveFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "behaviour_exercise_plan":
                ActionPlanFlow actionPlanFlow = new ActionPlanFlow(engine.getEngine());
                String actionPlanFlowId = actionPlanFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(actionPlanFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "goal_progress": // 目标进展
                GoalProgressFlow goalProgressFlow = new GoalProgressFlow(engine.getEngine());
                String goalProgressFlowId = goalProgressFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(goalProgressFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            case "strategy_practice": //对策游戏
                StrategyPracticeFlow strategyPracticeFlow = new StrategyPracticeFlow(engine.getEngine());
                String strategyPracticeFlowId = strategyPracticeFlow.deploy(flowDayitemDO.getId(), flowDayitemDO.getSettingsObj());
                flowDayitemDO.setTaskFlowId(strategyPracticeFlowId);
                treatmentFlowDayitemMapper.updateById(flowDayitemDO);
                break;
            default:
                throw new RuntimeException("not supported task flow");
        }
    }

    @Override
    public Map getNext(Long userId, Long treatmentInstanceId, Long dayItemInstanceId){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        BaseFlow taskFlow = getTaskFlow(flowDayitemDO);
        Container container = getContainer(taskFlow, dayitemInstanceDO, flowDayitemDO);
        if(dayitemInstanceDO.getTaskInstanceId().isEmpty()){
            dayitemInstanceDO.setTaskInstanceId(container.getProcessInstanceId());
            treatmentDayitemInstanceMapper.updateById(dayitemInstanceDO);
        }
        if(taskFlow.prerequisiteReady(container)){
            return taskFlow.run(container);
        }else{
            return taskFlow.prerequisiteFailed(container);
        }
    }

    @Override
    public void userSubmit(BaseFlow taskFlow, Long dayitem_instance_id, String taskId, DayitemStepSubmitReqVO submitReqVO){
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayitem_instance_id);
        Container container = taskFlow.loadProcessInstance(dayitemInstanceDO.getTaskInstanceId());
        taskFlow.userSubmit(container, taskId, submitReqVO);
    }

    @Override
    public void notify(DelegateExecution execution) {
        Map<String, Object> variables =  execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        Long userId = treatmentDayitemInstanceDO.getUserId();
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        BaseFlow taskFlow = getTaskFlow(flowDayitemDO);
        taskFlow.loadProcessInstance(treatmentDayitemInstanceDO.getTaskInstanceId());
        taskFlow.onFlowEnd(execution);
    }

}
