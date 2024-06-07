package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

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
        queryWrapper.in(TreatmentDayitemInstanceDO::getId , flowDayitemDOS.stream().map(TreatmentFlowDayitemDO::getId).toArray());
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


    default void finishDayItemInstance(Long dayItemInstanceId){
        TreatmentDayitemInstanceDO instanceDO = selectById(dayItemInstanceId);
        instanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
        updateById(instanceDO);
    }

}
