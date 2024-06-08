package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentChatHistoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO.MAIN_TREATMENT_DAYITEM_INSTANCE_ID;
import static com.github.yulichang.toolkit.JoinWrappers.lambda;

@Service
public class TreatmentChatHistoryServiceImpl implements TreatmentChatHistoryService{

    @Resource
    TreatmentChatHistoryMapper treatmentChatHistoryMapper;

    @Override
    public void addChatHistory(Long userId, Long treatmentInstanceId, TreatmentNextVO nextVO, boolean isSystem) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(nextVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        TreatmentChatHistoryDO treatmentChatHistoryDO = new TreatmentChatHistoryDO();
        treatmentChatHistoryDO.setUserId(userId);
        treatmentChatHistoryDO.setTreatmentInstanceId(treatmentInstanceId);
        treatmentChatHistoryDO.setMessage(jsonString);
        treatmentChatHistoryDO.setSystem(isSystem);
        treatmentChatHistoryDO.setTreatmentDayitemInstanceId(MAIN_TREATMENT_DAYITEM_INSTANCE_ID);
        treatmentChatHistoryMapper.insert(treatmentChatHistoryDO);
    }

    @Override
    public List<TreatmentChatHistoryDO> queryChatHistory(Long userId, Long treatmentInstanceId) {
        return treatmentChatHistoryMapper.queryChatHistory(userId, treatmentInstanceId);
    }

    @Override
    public void addTaskChatHistory(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemNextStepRespVO stepRespVO, boolean isSystem) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(stepRespVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        TreatmentChatHistoryDO treatmentChatHistoryDO = new TreatmentChatHistoryDO();
        treatmentChatHistoryDO.setUserId(userId);
        treatmentChatHistoryDO.setTreatmentInstanceId(treatmentInstanceId);
        treatmentChatHistoryDO.setMessage(jsonString);
        treatmentChatHistoryDO.setSystem(isSystem);
        treatmentChatHistoryDO.setTreatmentDayitemInstanceId(treatmentDayitemInstanceId);
        treatmentChatHistoryMapper.insert(treatmentChatHistoryDO);
    }

    @Override
    public List<TreatmentChatHistoryDO> queryTaskChatHistory(Long userId, Long treatmentDayitemInstanceId) {
        return treatmentChatHistoryMapper.queryTaskChatHistory(userId, treatmentDayitemInstanceId);
    }
}
