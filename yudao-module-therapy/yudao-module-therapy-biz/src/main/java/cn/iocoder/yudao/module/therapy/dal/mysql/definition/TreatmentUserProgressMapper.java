package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentUserProgressDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentUserProgressMapper extends BaseMapperX<TreatmentUserProgressDO> {

    default TreatmentUserProgressDO getUserCurrentProgress(Long userId, Long treatmentInstanceId) {
        return selectOne("user_id", userId, "treatment_instance_id", treatmentInstanceId);
    }

}
