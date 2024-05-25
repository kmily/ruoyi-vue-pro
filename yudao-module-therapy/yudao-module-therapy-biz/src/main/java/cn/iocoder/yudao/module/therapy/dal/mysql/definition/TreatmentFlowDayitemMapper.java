package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface TreatmentFlowDayitemMapper extends BaseMapperX<TreatmentFlowDayitemDO> {

    default List<TreatmentFlowDayitemDO> getFirstFlowDayitems(Long dayId) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).orderByAsc("group_seq");
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> filterByDay(Long dayId) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId);
        return selectList(queryWrapper);
    }

    default boolean hasNextGroup(Long dayId, Integer groupSeq) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).gt("group_seq", groupSeq)
                .orderByAsc("group_seq").last("limit 1");
        return selectCount(queryWrapper) > 0;
    }

    default List<TreatmentFlowDayitemDO> getNextGroup(Long dayId, Integer groupSeq) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).gt("group_seq", groupSeq)
                .orderByAsc("group_seq");
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> selectGroupItems(Long dayId, Integer groupSeq) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("group_seq", groupSeq);
        return selectList(queryWrapper);
    }
}
