package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentFlowMapper extends BaseMapperX<TreatmentFlowDO> {

    default TreatmentFlowDO selectByCode(String code) {
        return selectOne("code", code);
    }

}
