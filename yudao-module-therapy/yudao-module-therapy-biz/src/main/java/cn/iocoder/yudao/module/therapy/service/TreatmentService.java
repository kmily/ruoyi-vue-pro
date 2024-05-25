package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.GetNextRespVO;
import cn.iocoder.yudao.module.therapy.service.common.StepResp;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

public interface TreatmentService {
    Long initTreatmentInstance(Long userId, String treatmentCode);

    Long getCurrentTreatmentInstance(Long userId, String treatmentCode);

    StepResp getNext(TreatmentStepItem userCurrentStep);

    void completeDayitemInstance(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);
}
