package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentUserProgressDO;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

public interface TreatmentUserProgressService {
    TreatmentStepItem getTreatmentUserProgress(Long userId, Long treatmentInstanceId);
}
