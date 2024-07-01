package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;

import java.util.Map;

public interface TaskFlowService {
    Map getNext(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);

    void createBpmnModel(Long flowDayitemId);

    public void userSubmit(BaseFlow taskFlow, Long dayitem_instance_id, String taskId, DayitemStepSubmitReqVO submitReqVO);

    public BaseFlow getTaskFlow(TreatmentFlowDayitemDO flowDayitemDO);

    public BaseFlow getTaskFlow(Long userId,  Long dayItemInstanceId);

    public void updateFlowFromDayitem(TreatmentFlowDayitemDO flowDayitemDO, String type);
}
