package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TTMainInsertedStepDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TTMainInsertedStepMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;


@Component("scaleFlowSystemResponse")
public class ScaleFlowSystemResponse implements org.flowable.engine.delegate.JavaDelegate {

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private TreatmentService treatmentService;

    public void execute(DelegateExecution execution) {
        Long dayItemInstanceId = (Long) execution.getVariable(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);

        String content = "量表评估后的系统回复消息";
        treatmentService.addGuideLanguageStep(dayitemInstanceDO.getUserId(), dayitemInstanceDO.getFlowInstanceId(),
                content);

        content = "目前是样例，增加逻辑，TODO";
        treatmentService.addGuideLanguageStep(dayitemInstanceDO.getUserId(), dayitemInstanceDO.getFlowInstanceId(),
                content);
    }
}
