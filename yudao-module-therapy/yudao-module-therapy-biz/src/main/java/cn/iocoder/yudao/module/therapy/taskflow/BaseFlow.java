package cn.iocoder.yudao.module.therapy.taskflow;

import java.util.Map;

public interface BaseFlow {

    Map run();

    String deploy(Long id, Map<String, Object> settings);

    String createProcessInstance(String bpmnName);

    void loadProcessInstance(String taskInstanceId);

    void userSubmit(String taskId, Map<String, Object> variables);
}
