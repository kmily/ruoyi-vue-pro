package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TreatmentInstanceMapper extends BaseMapperX<TreatmentInstanceDO> {
    default TreatmentInstanceDO initInstance(Long userId, TreatmentFlowDO treatment){
        List<TreatmentInstanceDO> instanceList = filterCurrentByUserAndTreatment(userId, treatment.getId());
        if (instanceList.size() > 0){
            return instanceList.get(0);
        }
        // Else init a new instance
        TreatmentInstanceDO instanceDO = new TreatmentInstanceDO();
        instanceDO.setUserId(userId);
        instanceDO.setFlowId(treatment.getId());
        instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue());
        insert(instanceDO);
        return instanceDO;
    }

    default List<TreatmentInstanceDO> filterCurrentByUserAndTreatment(Long userId, Long treatmentId){
        return selectList(new LambdaQueryWrapper<TreatmentInstanceDO>()
                .eq(TreatmentInstanceDO::getUserId, userId)
                .eq(TreatmentInstanceDO::getFlowId, treatmentId)
                .in(TreatmentInstanceDO::getStatus,
                        TreatmentInstanceDO.TreatmentStatus.NOT_STARTED.getValue(),
                        TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue(),
                        TreatmentInstanceDO.TreatmentStatus.IN_PROGRESS.getValue()));
    }

    default TreatmentInstanceDO getLatestByUserId(Long userId){
        LambdaQueryWrapper<TreatmentInstanceDO> queryWrapper= Wrappers.lambdaQuery(TreatmentInstanceDO.class)
                .eq(TreatmentInstanceDO::getUserId,userId)
                .orderByDesc(TreatmentInstanceDO::getId)
                .last("LIMIT 1");
        return selectOne(queryWrapper);
    }

    default int countInTreatment(Long flowId){
        LambdaQueryWrapper<TreatmentInstanceDO> queryWrapper = Wrappers.lambdaQuery(TreatmentInstanceDO.class)
                .eq(TreatmentInstanceDO::getFlowId, flowId)
                .in(TreatmentInstanceDO::getStatus,
                        TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue(),
                        TreatmentInstanceDO.TreatmentStatus.IN_PROGRESS.getValue());
        return selectCount(queryWrapper).intValue();
    }

    default int countFinishedTreatment(Long flowId){
        LambdaQueryWrapper<TreatmentInstanceDO> queryWrapper = Wrappers.lambdaQuery(TreatmentInstanceDO.class)
                .eq(TreatmentInstanceDO::getFlowId, flowId)
                .eq(TreatmentInstanceDO::getStatus, TreatmentInstanceDO.TreatmentStatus.COMPLETED.getValue());
        return selectCount(queryWrapper).intValue();
    }

    default void endTreatment(Long userId){
        List<TreatmentInstanceDO> instanceDOS = selectList(
                new LambdaQueryWrapper<TreatmentInstanceDO>()
                        .eq(TreatmentInstanceDO::getUserId, userId)
                        .in(TreatmentInstanceDO::getStatus,
                                TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue(),
                                TreatmentInstanceDO.TreatmentStatus.NOT_STARTED.getValue(),
                                TreatmentInstanceDO.TreatmentStatus.IN_PROGRESS.getValue())
        );
        for(TreatmentInstanceDO instanceDO: instanceDOS){
            instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.COMPLETED.getValue());
            updateById(instanceDO);
        }
    }


}
