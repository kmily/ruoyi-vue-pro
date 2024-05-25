package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserExtDTO;
import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentFlowMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentInstanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Override
    public Long initTreatment(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        TreatmentInstanceDO instance = treatmentInstanceMapper.initInstance(userId, treatmentFlow);
        return instance.getId();
    }

    @Override
    public boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO) {
        MemberUserExtDTO dto= memberUserApi.getUserExtInfo(userId);
        if (dto != null) {
            dto.setAppointmentDate(reqVO.getAppointmentDate());
            dto.setAppointmentTimeRange(reqVO.getAppointmentPeriodTime());
            memberUserApi.updateMemberExtByUserId(dto);
        } else {
            memberUserApi.saveUserExtInfo(dto);
        }

        return true;
    }
}
