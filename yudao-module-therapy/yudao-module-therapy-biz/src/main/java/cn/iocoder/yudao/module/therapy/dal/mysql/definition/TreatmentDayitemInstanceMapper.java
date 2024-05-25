package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface TreatmentDayitemInstanceMapper extends BaseMapperX<TreatmentDayitemInstanceDO> {
    default List<TreatmentDayitemInstanceDO> initInstances(Long userId, List<TreatmentFlowDayitemDO> flowDayitemDOS){
        ArrayList<TreatmentDayitemInstanceDO> instances = new ArrayList<>();
        for (TreatmentFlowDayitemDO flowDayitemDO : flowDayitemDOS) {
            TreatmentDayitemInstanceDO instanceDO = new TreatmentDayitemInstanceDO();
            instanceDO.setUser_id(userId);
            instanceDO.setDayitem_id(flowDayitemDO.getId());
            instanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.INITIATED.getValue());
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
        queryWrapper.eq(TreatmentDayitemInstanceDO::getUser_id, userId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getFlow_instance_id, flowInstanceId);
        queryWrapper.in(TreatmentDayitemInstanceDO::getDayitem_id, flowDayitemDOS.stream().map(TreatmentFlowDayitemDO::getId).toArray());
        return selectList(queryWrapper);
    }

    default TreatmentDayitemInstanceDO selectByUserIdAndId(Long userId, Long dayItemInstanceId){
        LambdaQueryWrapper<TreatmentDayitemInstanceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TreatmentDayitemInstanceDO::getUser_id, userId);
        queryWrapper.eq(TreatmentDayitemInstanceDO::getId, dayItemInstanceId);
        return selectOne(queryWrapper);
    }


}
