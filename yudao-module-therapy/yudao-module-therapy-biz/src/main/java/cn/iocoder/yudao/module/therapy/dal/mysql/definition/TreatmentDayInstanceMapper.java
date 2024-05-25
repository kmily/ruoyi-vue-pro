package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TreatmentDayInstanceMapper extends BaseMapperX<TreatmentDayInstanceDO> {
    default TreatmentDayInstanceDO initInstance(Long userId, Long day_id, Long flow_id){
        TreatmentDayInstanceDO instanceDO = new TreatmentDayInstanceDO();
        instanceDO.setUser_id(userId);
        instanceDO.setFlow_instance_id(flow_id);
        instanceDO.setDay_id(day_id);
        instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue());
        insert(instanceDO);
        return instanceDO;
    }
}
