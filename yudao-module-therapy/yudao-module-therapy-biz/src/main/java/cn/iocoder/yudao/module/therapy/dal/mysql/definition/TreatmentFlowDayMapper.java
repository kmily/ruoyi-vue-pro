package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

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
        queryWrapper = queryWrapper.eq("flow_id", currentDay.getFlowId()).gt("sequence", currentDay.getSequence())
                .orderByAsc("sequence").last("limit 1");
        return selectOne(queryWrapper);
    }
}
