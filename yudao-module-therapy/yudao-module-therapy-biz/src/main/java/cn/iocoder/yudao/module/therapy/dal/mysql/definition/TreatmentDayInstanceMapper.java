package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentDayInstanceMapper extends BaseMapperX<TreatmentDayInstanceDO> {
    default TreatmentDayInstanceDO initInstance(Long userId, Long day_id, Long flow_instance_id){
        TreatmentDayInstanceDO instanceDO = new TreatmentDayInstanceDO();
        instanceDO.setUserId(userId);
        instanceDO.setFlowInstanceId(flow_instance_id);
        instanceDO.setDayId(day_id);
        instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue());
        insert(instanceDO);
        return instanceDO;
    }
}
