package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GuideStepDelegate  implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        String guideText = execution.getCurrentFlowElement().getAttributeValue("content", "content");
        System.out.println("Guide Text: " + guideText);
    }

}
