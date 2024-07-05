package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TTMainInsertedStepDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TTMainInsertedStepMapper  extends BaseMapperX<TTMainInsertedStepDO>{

    default List<TTMainInsertedStepDO> getInsertedNextVO(Long userId, Long treatmentInstanceId){
        return selectList(new LambdaQueryWrapper<TTMainInsertedStepDO>()
                .eq(TTMainInsertedStepDO::getUserId, userId)
                .eq(TTMainInsertedStepDO::getTreatmentInstanceId, treatmentInstanceId)
                .eq(TTMainInsertedStepDO::getStatus, TTMainInsertedStepDO.StatusEnum.DEFAULT.getValue())
                .orderByAsc(TTMainInsertedStepDO::getSequence)
        );
    }
}
