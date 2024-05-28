package cn.iocoder.yudao.module.therapy.service;

import java.util.Map;

public interface TaskFlowService {
    Map getNext(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);

    void createBpmnModel(Long flowDayitemId);

    void userSubmit(Long dayitem_instance_id, String taskId, Map<String, Object> variables);
}
