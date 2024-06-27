package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentFlowMapper extends BaseMapperX<TreatmentFlowDO> {

    default TreatmentFlowDO selectByCode(String code) {
        return selectOne("code", code);
    }

    default PageResult<TreatmentFlowDO> selectPage(PageParam reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentFlowDO>()
                .orderByDesc(TreatmentFlowDO::getId));
    }
}
