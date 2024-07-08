package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;


@Component
public class RelaxExerciseFlow extends BaseFlow {

    public RelaxExerciseFlow(ProcessEngine engine) {
        super(engine);
    }

    @Override
    public String getProcessName(Long id) {
        return "VideoTreatment-" + id;
    }

    @Override
    public void onFlowEnd(DelegateExecution execution) {
        throw new RuntimeException("Not implemented");
    }

    public String deploy(Long id, Map<String, Object> extraSettings) {
        return "Not implemented";
    }


    @Override
    public void userSubmit(Container container, String taskId, DayitemStepSubmitReqVO submitReqVO) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Map run(Container container) {
        throw new RuntimeException("Not implemented");
    }

}
