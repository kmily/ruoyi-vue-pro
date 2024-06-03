package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;

import java.util.Map;

public interface TaskFlowService {
    Map getNext(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);

    void createBpmnModel(Long flowDayitemId);

    public void userSubmit(BaseFlow taskFlow, Long dayitem_instance_id, String taskId, Map<String, Object> variables);
    public BaseFlow getTaskFlow(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);
}
