package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TreatmentFlowDayMapper extends BaseMapperX<TreatmentFlowDayDO> {

    default TreatmentFlowDayDO getFirstFlowDay(Long flowId) {
        QueryWrapper<TreatmentFlowDayDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("flow_id", flowId).orderByAsc("sequence").last("limit 1");
        return selectOne(queryWrapper);
    }

    default TreatmentFlowDayDO getNextFlowDay(TreatmentFlowDayDO currentDay) {
        QueryWrapper<TreatmentFlowDayDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("flow_id", currentDay.getFlowId())
                .gt("sequence", currentDay.getSequence())
                .orderByAsc("sequence").last("limit 1");
        return selectOne(queryWrapper);
    }

    default boolean isLastFlowDay(TreatmentFlowDayDO currentDay) {
        QueryWrapper<TreatmentFlowDayDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("flow_id", currentDay.getFlowId())
                .gt("sequence", currentDay.getSequence());
        return selectCount(queryWrapper) == 0;
    }

    default List<TreatmentFlowDayDO> getPlanListByFlowId(Long id){
        LambdaQueryWrapper<TreatmentFlowDayDO> wrapper= Wrappers.lambdaQuery(TreatmentFlowDayDO.class)
                .eq(TreatmentFlowDayDO::getFlowId,id)
                .orderByAsc(TreatmentFlowDayDO::getSequence);
        return selectList(wrapper);
    }

    default int getDaysCount(Long flowId) {
        LambdaQueryWrapper<TreatmentFlowDayDO> wrapper = Wrappers.lambdaQuery(TreatmentFlowDayDO.class)
                .eq(TreatmentFlowDayDO::getFlowId, flowId);
        return Math.toIntExact(selectCount(wrapper));
    }

    @Select(" select * from hlgyy_treatment_flow_day d inner join " +
            " hlgyy_treatment_day_instance i on d.id = i.day_id " +
            " where i.id = #{instanceId}")
    TreatmentFlowDayDO selectByDayInstanceId(@Param("instanceId") Long instanceId);
}
