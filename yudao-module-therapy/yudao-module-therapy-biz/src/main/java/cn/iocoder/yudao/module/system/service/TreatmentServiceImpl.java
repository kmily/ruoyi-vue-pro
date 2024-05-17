package cn.iocoder.yudao.module.system.service;

import cn.iocoder.yudao.module.system.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.system.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.system.dal.mysql.definition.TreatmentFlowMapper;
import cn.iocoder.yudao.module.system.dal.mysql.definition.TreatmentInstanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Override
    public Long initTreatment(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        TreatmentInstanceDO instance = treatmentInstanceMapper.initInstance(userId, treatmentFlow);
        return instance.getId();
    }
}
