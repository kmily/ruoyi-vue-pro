package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import javax.annotation.Resource;
import java.util.List;

public class TreatmentUserProgressServiceImpl implements  TreatmentUserProgressService{

    @Resource
    TreatmentUserProgressMapper treatmentUserProgressMapper;

    @Resource
    TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Override
    public TreatmentStepItem getTreatmentUserProgress(Long userId, Long treatmentInstanceId) {
        TreatmentStepItem stepItem = new TreatmentStepItem();
        TreatmentInstanceDO instance = treatmentInstanceMapper.selectById(treatmentInstanceId);
        TreatmentUserProgressDO progressDO =  treatmentUserProgressMapper.getUserCurrentProgress(userId, treatmentInstanceId);
        stepItem.setFlowInstance(instance);
        if(progressDO != null){
            TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectById(progressDO.getDay_instance_id());
            stepItem.setDay(dayInstanceDO);

            List<TreatmentFlowDayitemDO> flowDayitemDOS = treatmentFlowDayitemMapper.selectGroupItems(dayInstanceDO.getDay_id(), progressDO.getGroup_seq());
            List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectCurrentItems(flowDayitemDOS);
            stepItem.setDay_items(dayitemInstanceDOS);
        }
        return stepItem;
    }

}
