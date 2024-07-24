package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentHistoryChatMessageVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;

import java.util.List;
import java.util.Map;

public interface TreatmentChatHistoryService {
    void addChatHistory(Long userId, Long treatmentInstanceId, TreatmentNextVO message, boolean isSystem);

    void addUserChatMessage(Long userId, Long treatmentInstanceId, Object msgObj);

    List<TreatmentChatHistoryDO> queryChatHistory(Long userId, Long treatmentInstanceId);

    void addTaskChatHistory(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemNextStepRespVO stepRespVO, boolean isSystem);

    void addTaskUserSubmitMessage(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemStepSubmitReqVO stepSubmitReqVO);

    void addTaskUserChatMessage(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, Object msgObj);

    void deleteByDayItemInstanceId(Long dayItemInstanceId);

    List<TreatmentChatHistoryDO> queryTaskChatHistory(Long userId, Long treatmentInstanceId);

    List<TreatmentHistoryChatMessageVO> convert(List<TreatmentChatHistoryDO> list);
}