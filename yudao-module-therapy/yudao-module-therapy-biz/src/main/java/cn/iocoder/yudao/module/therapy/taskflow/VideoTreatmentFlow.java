package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;

@Component
public class VideoTreatmentFlow extends BaseFlow{


    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    SurveyService surveyService;

    @Resource
    TreatmentStatisticsDataService treatmentStatisticsDataService;
    @Resource
    private TreatmentService treatmentService;

    public VideoTreatmentFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "VideoTreatment-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        Map variables = execution.getVariables();
        Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
        treatmentService.finishDayItemInstance(dayItemInstanceId);
    }

    public String deploy(Long id, Map<String, Object> extraSettings) {
        Map settings = super.loadSettings("/goal_progress.json");
        ArrayList<Map> steps = (ArrayList<Map>) settings.get("steps");
        Map watchStepSetting = new HashMap();
        watchStepSetting.put("step_type", "watch_video_qst");
        watchStepSetting.put("step_data", extraSettings);
        steps.add(watchStepSetting);
        settings.put("steps", steps);
        return super.deploy(settings, id);
    }

    public Map<String, Object> auto_watch_video_qst(Container container, Map data, Task currentTask) {
        return data;
    }

    public void submit_watch_video_qst(Container container, DayitemStepSubmitReqVO submitReqVO, Task currentTask) {
        surveyService.submitSurveyForFlow(submitReqVO.getStep_data().getSurvey());
    }
}
