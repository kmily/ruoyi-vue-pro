package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TreatmentFlowMapper extends BaseMapperX<TreatmentFlowDO> {

    default TreatmentFlowDO selectByCode(String code) {
        return selectOne("code", code);
    }

    default PageResult<TreatmentFlowDO> selectPage(PageParam reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentFlowDO>()
                .orderByDesc(TreatmentFlowDO::getId));
    }

    @Select(
            "select code from hlgyy_treatment_flow f inner join " +
                    " hlgyy_treatment_instance i on f.id = i.flow_id " +
                    " inner join hlgyy_treatment_dayitem_instance di on i.id = di.flow_instance_id " +
                    " where di.id = #{dayitemInstanceId} limit 1"
    )
    String getFlowCodeByDayitemInstanceId(@Param("dayitemInstanceId") Long dayitemInstanceId);
}
