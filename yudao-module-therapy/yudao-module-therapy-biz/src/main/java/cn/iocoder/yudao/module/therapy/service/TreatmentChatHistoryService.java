package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;

import java.util.List;
import java.util.Map;

public interface TreatmentChatHistoryService {
    void addChatHistory(Long userId, Long treatmentInstanceId, TreatmentNextVO message, boolean isSystem);

    List<TreatmentChatHistoryDO> queryChatHistory(Long userId, Long treatmentInstanceId);

    void addTaskChatHistory(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemNextStepRespVO stepRespVO, boolean isSystem);

    List<TreatmentChatHistoryDO> queryTaskChatHistory(Long userId, Long treatmentInstanceId);

}
