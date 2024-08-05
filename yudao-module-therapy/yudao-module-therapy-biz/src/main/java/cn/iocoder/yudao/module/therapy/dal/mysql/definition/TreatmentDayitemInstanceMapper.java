package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface TreatmentDayitemInstanceMapper extends BaseMapperX<TreatmentDayitemInstanceDO> {
    default List<TreatmentDayitemInstanceDO> initInstances(Long userId, TreatmentDayInstanceDO dayInstanceDO, List<TreatmentFlowDayitemDO> flowDayitemDOS){
        ArrayList<TreatmentDayitemInstanceDO> instances = new ArrayList<>();
        for (TreatmentFlowDayitemDO flowDayitemDO : flowDayitemDOS) {
            TreatmentDayitemInstanceDO instanceDO = new TreatmentDayitemInstanceDO();
            instanceDO.setUserId(userId);
            instanceDO.setDayitemId(flowDayitemDO.getId());
            instanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.INITIATED.getValue());
            instanceDO.setFlowInstanceId(dayInstanceDO.getFlowInstanceId());
            instanceDO.setDayInstanceId(dayInstanceDO.getId());
            insert(instanceDO);
            instances.add(instanceDO);
        }
        return instances;
    }

    default List<TreatmentDayitemInstanceDO> selectCurrentItems( List<TreatmentFlowDayitemDO> flowDayitemDOS){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TreatmentDayitemInstanceDO::getDayitemId , flowDayitemDOS.stream().map(TreatmentFlowDayitemDO::getId).toArray());
        return selectList(queryWrapper);
    }


    default List<TreatmentDayitemInstanceDO> getUserDayitemInstances(Long userId, Long flowInstanceId, List<TreatmentFlowDayitemDO> flowDayitemDOS){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TreatmentDayitemInstanceDO::getUserId, userId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getFlowInstanceId, flowInstanceId);
        queryWrapper.in(TreatmentDayitemInstanceDO::getDayitemId, flowDayitemDOS.stream().map(TreatmentFlowDayitemDO::getId).toArray());
        return selectList(queryWrapper);
    }

    default TreatmentDayitemInstanceDO selectByUserIdAndId(Long userId, Long dayItemInstanceId){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TreatmentDayitemInstanceDO::getUserId, userId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getId, dayItemInstanceId);
        return selectOne(queryWrapper);
    }


    default void finishDayItemInstanceDO(Long dayItemInstanceId){
        TreatmentDayitemInstanceDO instanceDO = selectById(dayItemInstanceId);
        instanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
        updateById(instanceDO);
    }

    default int countByFinishedDayitemId(Long dayItemId){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TreatmentDayitemInstanceDO::getDayitemId, dayItemId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getStatus, TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
        return selectCount(queryWrapper).intValue();
    }

    @Select("select i.* from hlgyy_treatment_dayitem_instance i " +
            " inner join " +
            " hlgyy_treatment_flow_dayitem d on i.dayitem_id = d.id " +
            " inner join hlgyy_treatment_flow f on f.id = d.flow_id" +
            " where user_id = #{userId} and  d.type = #{taskType} and f.code = #{flowCode}   " +
            " order by i.create_time desc limit 1")
    TreatmentDayitemInstanceDO queryUserGoalAndMotiveInstance(@Param("userId") Long userId,
                                                              @Param("taskType") Integer taskType,
                                                              @Param("flowCode") String flowCode);

    @Select({
            "<script>",
            "select i.* from hlgyy_treatment_dayitem_instance i",
            "inner join hlgyy_treatment_flow_dayitem d on i.dayitem_id = d.id",
            "where i.flow_instance_id in",
            "<foreach item='item' index='index' collection='flowInstanceIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "and d.type = 3", // TaskType.PROBLEM_GOAL_MOTIVE
            "</script>"
    })
    List<TreatmentDayitemInstanceDO> findGoalAndMotiveItems(@Param("flowInstanceIds") List<Long> flowInstanceIds);

    default TreatmentDayitemInstanceDO queryInstance(Long userId, Long dayItemInstanceId){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TreatmentDayitemInstanceDO::getUserId, userId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getId, dayItemInstanceId);
        return selectOne(queryWrapper);
    }

//    default List<TreatmentDayitemInstanceDO> queryInstances( List<Long> dayItemIds){
//        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
//        // TODO whether order matters?
//        queryWrapper.in(TreatmentDayitemInstanceDO::getDayitemId, dayItemIds);
//        return selectList(queryWrapper);
//    }

    default List<TreatmentDayitemInstanceDO> queryInstances(Long treatmentInstanceId, List<Long> dayItemIds){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        // TODO whether order matters?
        queryWrapper.in(TreatmentDayitemInstanceDO::getDayitemId, dayItemIds);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getFlowInstanceId, treatmentInstanceId);
        return selectList(queryWrapper);
    }
}
