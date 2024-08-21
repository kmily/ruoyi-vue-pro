package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TreatmentFlowDayitemMapper extends BaseMapperX<TreatmentFlowDayitemDO> {

    default List<TreatmentFlowDayitemDO> getFirstGroupFlowDayitems(Long dayId) {
        Integer agroup = selectOne(new QueryWrapper<TreatmentFlowDayitemDO>().eq("day_id", dayId).orderByAsc("agroup").last("limit 1")).getAgroup();
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", agroup);
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> filterByDay(Long dayId) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId);
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> queryTasks(Long dayId){
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId);
        queryWrapper = queryWrapper.ne("type", TaskType.GUIDE_LANGUAGE.getType());
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> queryRequiredTasks(Long dayId){
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("required", true);
        queryWrapper = queryWrapper.ne("type", TaskType.GUIDE_LANGUAGE.getType());
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
        TreatmentFlowDayitemDO obj = selectOne(new QueryWrapper<TreatmentFlowDayitemDO>()
                .eq("day_id", dayId)
                .gt("agroup", agroup)
                .orderByAsc("agroup").last("limit 1"));
        if(obj == null){
            return null;
        }
        Integer nextAgroup = obj.getAgroup();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", nextAgroup)
                .orderByAsc("id"); // should be ordered by group_seq
        return selectList(queryWrapper);
    }

    /**
     * 获取当天下一组的所有任务
     * @param dayId
     * @param agroup
     * @return
     */
    default List<TreatmentFlowDayitemDO> getCurrentGroup(Long dayId, Long agroup) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", agroup)
                .orderByAsc("id"); // should be ordered by group_seq
        return selectList(queryWrapper);
    }

    default List<TreatmentFlowDayitemDO> selectGroupItems(Long dayId, Long agroup) {
        QueryWrapper<TreatmentFlowDayitemDO> queryWrapper = new QueryWrapper();
        queryWrapper = queryWrapper.eq("day_id", dayId).eq("agroup", agroup)
                .orderByAsc("id"); // should be ordered by group_seq
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

    default List<TreatmentFlowDayitemDO> getListByDayId(Long id){
        LambdaQueryWrapper<TreatmentFlowDayitemDO> queryWrapper=Wrappers.lambdaQuery(TreatmentFlowDayitemDO.class)
                .eq(TreatmentFlowDayitemDO::getDayId,id)
                .orderByAsc(TreatmentFlowDayitemDO::getFlowId);
        return selectList(queryWrapper);
    }
//    di.id, di.day_id, d.flow_id, d.name, d.sequence, ins.status
    @Select("select d.id as day_id, d.has_break, d.sequence as flow_day_index, d.name as flow_day_name, " +
            " dins.id as day_instance_id, dins.status as day_instance_status, " +
            " di.type as dayitem_type, ins.id as dayitem_instance_id, di.id as dayitem_id, " +
            " ins.create_time, ins.update_time, ins.status as dayitem_instance_status " +
            " from hlgyy_treatment_flow_days   d " +
            " left join hlgyy_treatment_flow_dayitem di " +
            " on di.day_id = d.id and  di.deleted != 1 " +
            " left join hlgyy_treatment_day_instance dins " +
            " on dins.day_id = d.id and dins.flow_instance_id = #{treatmentInstanceId}" +
            " left join hlgyy_treatment_dayitem_instance ins" +
            " on ins.flow_instance_id = #{treatmentInstanceId} and ins.dayitem_id = di.id " +
            " where d.flow_id = #{treatmentFlowId} and d.deleted != 1" +
            " order by d.sequence, di.agroup")
    List<TreatmentDayitemDetailDO> getDayitemDetail(@Param("treatmentInstanceId") Long treatmentInstanceId, @Param("treatmentFlowId") Long treatmentFlowId);



    @Select("select d.id as day_id, d.has_break, d.sequence as flow_day_index, d.name as flow_day_name, " +
            " dins.id as day_instance_id, dins.status as day_instance_status, " +
            " di.type as dayitem_type, ins.id as dayitem_instance_id, " +
            " ins.create_time, ins.update_time, ins.status as dayitem_instance_status " +
            " from hlgyy_treatment_flow_days   d " +
            " left join hlgyy_treatment_flow_dayitem di " +
            " on di.day_id = d.id  " +
            " left join hlgyy_treatment_day_instance dins " +
            " on dins.day_id = d.id and dins.flow_instance_id = #{treatmentInstanceId}" +
            " left join hlgyy_treatment_dayitem_instance ins" +
            " on ins.flow_instance_id = #{treatmentInstanceId} and ins.dayitem_id = di.id " +
            " where d.flow_id = #{treatmentFlowId} order by d.sequence, di.agroup and d.id = #{dayId}")
    List<TreatmentDayitemDetailDO> getDayitemDetailOfDay(@Param("treatmentInstanceId") Long treatmentInstanceId,
                                                         @Param("treatmentFlowId") Long treatmentFlowId,
                                                         @Param("dayId") Long dayId);


    @Select("select * from hlgyy_treatment_flow_dayitem where flow_id = #{flowId} and `type` = #{type} order by group_seq desc limit 1")
    TreatmentFlowDayitemDO getPriorEvaluation(@Param("flowId") Long flowId, @Param("type") Integer type);

    default List<TreatmentFlowDayitemDO> getListByFlowId(Long flowId){
        LambdaQueryWrapper<TreatmentFlowDayitemDO> queryWrapper=Wrappers.lambdaQuery(TreatmentFlowDayitemDO.class)
                .eq(TreatmentFlowDayitemDO::getFlowId,flowId);
        return selectList(queryWrapper);
    }
}
