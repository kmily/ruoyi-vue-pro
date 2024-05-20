package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentInstanceMapper extends BaseMapperX<TreatmentInstanceDO> {
    default TreatmentInstanceDO initInstance(Long userId, TreatmentFlowDO treatment){
        TreatmentInstanceDO instanceDO = new TreatmentInstanceDO();
        instanceDO.setUser_id(userId);
        instanceDO.setFlow_id(treatment.getId());
        Long id = Long.valueOf(insert(instanceDO));
        instanceDO.setId(id);
        return instanceDO;
    }
}
