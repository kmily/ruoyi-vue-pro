package cn.iocoder.yudao.module.therapy.taskflow;


import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import org.flowable.bpmn.model.Task;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;

@Component
public class MoodScoreFlow extends BaseFlow {
    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    public MoodScoreFlow(ProcessEngine engine) {
        super(engine);
    }


    @Override
    public String getProcessName(Long id) {
        return "MOOD_SCORE-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentDayitemInstanceMapper.finishDayItemInstance(dayItemInstanceId);
    }

    public Map<String, Object> auto_mood_ruler_qst(Container container,Map data, Task currentTask){
        return data;
    }

    public void submit_mood_ruler_qst(Container container,Map variables, Task currentTask){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        int moodScore = (int) variables.get("moodScore");
        runtimeService.setVariable(container.getProcessInstanceId(), "moodScore", moodScore);
    }

    public void auto_mood_respond(Container container,Map data, Task currentTask){
        data.put("content", "你的心情评分是" + data.get("moodScore"));
    }

    public void auto_mood_diary_qst(Container container,Map data, Task currentTask){
        data.put("content", "请填写今天的心情日记");
    }
}
