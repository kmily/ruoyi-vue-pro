package cn.iocoder.yudao.module.therapy.dal.mysql.definition;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO.MAIN_TREATMENT_DAYITEM_INSTANCE_ID;

@Mapper
public interface TreatmentChatHistoryMapper extends BaseMapperX<TreatmentChatHistoryDO> {

    default List<TreatmentChatHistoryDO> queryChatHistory(Long userId, Long treatmentInstanceId) {
        LambdaQueryWrapper<TreatmentChatHistoryDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(TreatmentChatHistoryDO::getUserId, userId)
                .eq(TreatmentChatHistoryDO::getTreatmentInstanceId, treatmentInstanceId)
                .eq(TreatmentChatHistoryDO::getTreatmentDayitemInstanceId, MAIN_TREATMENT_DAYITEM_INSTANCE_ID);
        return selectList(queryWrapper);
    }

    default List<TreatmentChatHistoryDO> queryTaskChatHistory(Long userId, Long treatmentDayitemInstanceId) {
        LambdaQueryWrapper<TreatmentChatHistoryDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(TreatmentChatHistoryDO::getUserId, userId)
                .eq(TreatmentChatHistoryDO::getTreatmentDayitemInstanceId, treatmentDayitemInstanceId);
        return selectList(queryWrapper);
    }
}
