package cn.iocoder.yudao.module.therapy.taskflow;

import lombok.Data;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;


@Data
public class Container {
    private ProcessInstance processInstance;

    private HistoricProcessInstance historicProcessInstance;

    public String getProcessInstanceId(){
        return processInstance.getId();
    }

    public Container() {}
}
