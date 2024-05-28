package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import com.alibaba.druid.wall.WallCheckResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface TreatmentFlowDayitemMapper extends BaseMapperX<TreatmentFlowDayitemDO> {

    default List<TreatmentFlowDayitemDO> getFirstGroupFlowDayitems(Long dayId) {
        Long agroup = selectOne(new QueryWrapper<TreatmentFlowDayitemDO>().eq("day_id", dayId).orderByAsc("agroup").last("limit 1")).getAgroup();
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", agroup);
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> filterByDay(Long dayId) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId);
        return selectList(queryWrapper);
    }

    default boolean hasNextGroup(Long dayId, Long agroup) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).gt("agroup", agroup)
                .orderByAsc("agroup").last("limit 1");
        return selectCount(queryWrapper) > 0;
    }

    /**
     * 获取当天下一组的所有任务
     * @param dayId
     * @param agroup
     * @return
     */
    default List<TreatmentFlowDayitemDO> getNextGroup(Long dayId, Long agroup) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        Long nextAgroup = selectOne(new QueryWrapper<TreatmentFlowDayitemDO>()
                .eq("day_id", dayId)
                .gt("agroup", agroup)
                .orderByAsc("agroup").last("limit 1")).getAgroup();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", nextAgroup)
                .orderByAsc("group_seq");
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> selectGroupItems(Long dayId, Long agroup) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", agroup).orderByAsc("group_seq");
        return selectList(queryWrapper);
    }

    /**
     * 通过计划删除所有子任务
     * @param id
     */
    default void deleteByDayId(Long id){
        LambdaQueryWrapper<TreatmentFlowDayitemDO> queryWrapper= Wrappers.lambdaQuery(TreatmentFlowDayitemDO.class)
                .eq(TreatmentFlowDayitemDO::getDayId,id);
        delete(queryWrapper);
    }
}
