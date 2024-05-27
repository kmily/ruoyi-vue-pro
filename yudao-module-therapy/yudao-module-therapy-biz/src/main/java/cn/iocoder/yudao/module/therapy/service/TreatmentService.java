package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import java.util.Map;
import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;


public interface TreatmentService {
    Long initTreatmentInstance(Long userId, String treatmentCode);

    Long getCurrentTreatmentInstance(Long userId, String treatmentCode);

    TreatmentStepItem getNext(TreatmentStepItem userCurrentStep);

    void completeDayitemInstance(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);

    boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO);
}
