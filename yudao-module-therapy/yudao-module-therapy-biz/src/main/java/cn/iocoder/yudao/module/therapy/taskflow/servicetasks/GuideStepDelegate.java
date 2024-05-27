package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.HashMap;

public class GuideStepDelegate  implements JavaDelegate {

    public void execute(DelegateExecution execution) {
//        String guideText = (String) execution.getVariableLocal("content");
                            //        String guideText ="lala";
                            //        System.out.println("Guide Text: " + execution.getVariables());
                            //        System.out.println("Guide Text: " + execution.getVariablesLocal());
                            //
                            //        ServiceTask serviceTask = (ServiceTask) execution.getCurrentFlowElement();
                            //        HashMap resp = new HashMap<>();
                            //        resp.put("item_type", "guide");
                            //        resp.put("content", guideText);
                            //        execution.setVariable("ServerResp", resp);
                            //        System.out.println("Guide Text: " + guideText);
    }

}
