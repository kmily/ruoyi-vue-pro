package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.VO.StepItemVO;
import cn.iocoder.yudao.module.therapy.controller.app.VO.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentUserProgressDO;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import java.util.Map;

public interface TreatmentUserProgressService {
    TreatmentStepItem getTreatmentUserProgress(Long userId, Long treatmentInstanceId);

    TreatmentNextVO convertStepItemToRespFormat(TreatmentStepItem stepItem);

    StepItemVO convertDayitemInstanceToMap(TreatmentDayitemInstanceDO dayitemInstanceDO);

    void updateUserProgress(TreatmentStepItem stepItem);

    void clearUserProgress(Long userId);
}
