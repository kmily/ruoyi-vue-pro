package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import org.flowable.engine.delegate.DelegateExecution;

import java.util.List;

public class AICheckUserTroublesCategory implements org.flowable.engine.delegate.JavaDelegate {
    public void execute(DelegateExecution execution) {
        List<String> userTroubles = (List<String>) execution.getVariable("troubles");
        // TODO check user troubles category
        System.out.println("User troubles: " + userTroubles);
        execution.setVariable("primary_troubles_is_set", false);
    }
}

