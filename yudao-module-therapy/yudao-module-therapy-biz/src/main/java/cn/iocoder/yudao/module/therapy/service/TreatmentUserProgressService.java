package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentUserProgressDO;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import java.util.Map;

public interface TreatmentUserProgressService {
    TreatmentStepItem getTreatmentUserProgress(Long userId, Long treatmentInstanceId);

    Map<String, Object> convertStepItemToRespFormat(TreatmentStepItem stepItem);

    Map<String, Object> convertDayitemInstanceToMap(TreatmentDayitemInstanceDO dayitemInstanceDO);

    void updateUserProgress(TreatmentStepItem stepItem);

    void clearUserProgress(Long userId);
}
