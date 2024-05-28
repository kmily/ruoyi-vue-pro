package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import org.flowable.engine.delegate.DelegateExecution;

import java.util.Map;

public class AICheckUserAgreementStepDelegate implements org.flowable.engine.delegate.JavaDelegate {
    public void execute(DelegateExecution execution) {
        String guideText = (String) execution.getVariable("UserAgreementText");
        if(guideText.contains("同意")){
            execution.setVariable("user_agree", true);
        }else{
            execution.setVariable("user_agree", false);
        }
    }
}

