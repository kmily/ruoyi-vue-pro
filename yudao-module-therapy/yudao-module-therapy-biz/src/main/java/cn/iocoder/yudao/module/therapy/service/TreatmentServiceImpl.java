package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.flowengine.DayTaskEngine;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import cn.iocoder.yudao.module.therapy.taskflow.GoalAndMotivationFlow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private  DayTaskEngine dayTaskEngine;

    @Override
    public Long initTreatmentInstance(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        TreatmentInstanceDO instance = treatmentInstanceMapper.initInstance(userId, treatmentFlow);
        return instance.getId();
    }

    @Override
    public Long getCurrentTreatmentInstance(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        List<TreatmentInstanceDO> instances = treatmentInstanceMapper.filterCurrentByUserAndTreatment(userId, treatmentFlow.getId());
        if (instances.size() > 0) {
            return instances.get(0).getId();
        } else {
            return -1L;
        }
    }

    @Override
    public TreatmentStepItem getNext(TreatmentStepItem userCurrentStep){
        dayTaskEngine.initWithCurrentStep(userCurrentStep);
        TreatmentStepItem nextStepItem = dayTaskEngine.getNextStepItem();

        return nextStepItem;
    }

    @Override
    public void completeDayitemInstance(Long userId, Long treatmentInstanceId, Long dayItemInstanceId){
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        treatmentDayitemInstanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
        treatmentDayitemInstanceMapper.updateById(treatmentDayitemInstanceDO);
    }

}
