package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;

public interface TreatmentService {
    Long initTreatment(Long userId, String treatmentCode);

    boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO);
}
