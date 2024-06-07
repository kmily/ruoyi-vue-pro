package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DayitemInstanceServiceImpl implements  DayitemInstanceService {

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;


    @Override
    public void updateDayitemInstanceExtAttr(Long dayitemInstanceId, String key, Object val){
        TreatmentDayitemInstanceDO instanceDO = treatmentDayitemInstanceMapper.selectById(dayitemInstanceId);
        instanceDO.updateExtAttr(key, val);
        treatmentDayitemInstanceMapper.updateById(instanceDO);
    }

}
