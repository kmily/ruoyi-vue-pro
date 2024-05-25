package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        instanceDO.setUser_id(userId);
        instanceDO.setFlow_id(treatment.getId());
        instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue());
        insert(instanceDO);
        return instanceDO;
    }

    default List<TreatmentInstanceDO> filterCurrentByUserAndTreatment(Long userId, Long treatmentId){
        return selectList(new LambdaQueryWrapper<TreatmentInstanceDO>()
                .eq(TreatmentInstanceDO::getUser_id, userId)
                .eq(TreatmentInstanceDO::getFlow_id, treatmentId)
                .in(TreatmentInstanceDO::getStatus,
                        TreatmentInstanceDO.TreatmentStatus.INITIATED.getValue(),
                        TreatmentInstanceDO.TreatmentStatus.IN_PROGRESS.getValue()));
    }

}
