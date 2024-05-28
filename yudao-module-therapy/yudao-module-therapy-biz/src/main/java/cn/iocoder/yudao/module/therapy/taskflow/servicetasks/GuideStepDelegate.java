package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.el.FixedValue;

import java.util.HashMap;

public class GuideStepDelegate  implements JavaDelegate {

    private FixedValue content;

    public void execute(DelegateExecution execution) {
//        System.out.println("Guide Text: " + execution.getVariablesLocal());
        String guideText = (String) content.getValue(execution);
        HashMap resp = new HashMap<>();
        resp.put("item_type", "guide");
        resp.put("content", guideText);
        execution.setVariable("ServerResp", resp);
        System.out.println("GuideText: " + guideText);
    }

}
