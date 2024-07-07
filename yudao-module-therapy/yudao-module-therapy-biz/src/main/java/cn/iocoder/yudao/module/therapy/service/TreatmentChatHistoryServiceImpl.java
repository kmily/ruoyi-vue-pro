package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentHistoryChatMessageVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentChatHistoryMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO.*;
import static com.github.yulichang.toolkit.JoinWrappers.lambda;

@Service
public class TreatmentChatHistoryServiceImpl implements TreatmentChatHistoryService{

    @Resource
    private TreatmentChatHistoryMapper treatmentChatHistoryMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Override
    public void addChatHistory(Long userId, Long treatmentInstanceId, TreatmentNextVO nextVO, boolean isSystem) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
    public void addUserChatMessage(Long userId, Long treatmentInstanceId, Object msgObj){
        insertChatHistory(userId, treatmentInstanceId, msgObj, false, SOURCE_USER, MAIN_TREATMENT_DAYITEM_INSTANCE_ID);
    }

    private String object2JsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
    private void insertChatHistory(Long userId, Long treatmentInstanceId, Object msgObj, boolean isSystem, String source, Long treatmentDayitemInstanceId) {
        String message = object2JsonString(msgObj);
        TreatmentChatHistoryDO treatmentChatHistoryDO = new TreatmentChatHistoryDO();
        treatmentChatHistoryDO.setUserId(userId);
        treatmentChatHistoryDO.setTreatmentInstanceId(treatmentInstanceId);
        treatmentChatHistoryDO.setMessage(message);
        treatmentChatHistoryDO.setSystem(isSystem);
        treatmentChatHistoryDO.setSource(source);
        treatmentChatHistoryDO.setTreatmentDayitemInstanceId(treatmentDayitemInstanceId);
        treatmentChatHistoryMapper.insert(treatmentChatHistoryDO);
    }

    @Override
    public void addTaskUserSubmitMessage(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemStepSubmitReqVO stepSubmitReqVO){
        insertChatHistory(userId, treatmentInstanceId, stepSubmitReqVO, false, SOURCE_USER_SUBMIT, treatmentDayitemInstanceId);
    }

    @Override
    public void addTaskUserChatMessage(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, Object msgObj){
        insertChatHistory(userId, treatmentInstanceId, msgObj, false, SOURCE_USER, treatmentDayitemInstanceId);
    }

    @Override
    public List<TreatmentChatHistoryDO> queryChatHistory(Long userId, Long treatmentInstanceId) {
        return treatmentChatHistoryMapper.queryChatHistory(userId, treatmentInstanceId);
    }

    @Override
    public void addTaskChatHistory(Long userId, Long treatmentInstanceId, Long treatmentDayitemInstanceId, DayitemNextStepRespVO stepRespVO, boolean isSystem) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(stepRespVO);
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            jsonString = "";
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

    private boolean isDayItemTask(Map stepItem) {
        return TaskType.allDayItemTypes().contains(stepItem.get("item_type")) &&
                !stepItem.get("item_type").equals(TaskType.GUIDE_LANGUAGE.getCode());
    }


    private String objToString(Map obj){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    private Map mergeTaskStatus(Map msgObj, Map<Long,  TreatmentDayitemInstanceDO.StatusEnum> instancesStatus){
            if(msgObj != null) {
                if(msgObj.get("step_item_type").equals("SINGLE")){
                    Map stepItem = (Map) msgObj.get("step_item");
                    stepItem.put("status",
                            instancesStatus.getOrDefault(
                                    Long.valueOf(stepItem.getOrDefault("id", "0").toString()),
                                    TreatmentDayitemInstanceDO.StatusEnum.INITIATED
                            )
                    );
                }else{
                    //List
                    List<Map> stepItems = (List<Map>) msgObj.get("step_items");
                    for(Map stepItem : stepItems){
                        stepItem.put("status",
                                instancesStatus.getOrDefault(
                                        Long.valueOf(stepItem.getOrDefault("id", "0").toString()),
                                        TreatmentDayitemInstanceDO.StatusEnum.INITIATED
                                )
                        );
                    }
                }
            }
            return msgObj;
    }


    public List<TreatmentHistoryChatMessageVO> convert(List<TreatmentChatHistoryDO> list) {
        List<TreatmentHistoryChatMessageVO> result = new ArrayList<>();
        Map<Long,  TreatmentDayitemInstanceDO.StatusEnum> instancesStatus = extractDayItemInstanceStatus(list);


        for (TreatmentChatHistoryDO item : list) {
            TreatmentHistoryChatMessageVO vo = new TreatmentHistoryChatMessageVO();
            Map msgObj = item.getMessageObj();
            msgObj = mergeTaskStatus(msgObj, instancesStatus);
            vo.setMessage(objToString(msgObj));
            vo.setIsSystem(item.isSystem());
            vo.setCreateTime(item.getCreateTime());
            vo.setSource(item.getSource());
            result.add(vo);
        }
        return result;
    }

    private Map<Long,  TreatmentDayitemInstanceDO.StatusEnum> extractDayItemInstanceStatus(List<TreatmentChatHistoryDO> list){
        List<Long> dayItemInstanceIds = new ArrayList<>();
        for (TreatmentChatHistoryDO item : list) {
            Map msgObj = item.getMessageObj();
            if(msgObj != null) {
                if(msgObj.get("step_item_type").equals("SINGLE")){
                    Map stepItem = (Map) msgObj.get("step_item");
                    if(isDayItemTask(stepItem)){
                        dayItemInstanceIds.add(Long.valueOf(stepItem.get("id").toString()));
                    }
                }else{
                    //List
                    List<Map> stepItems = (List<Map>) msgObj.get("step_items");
                    for(Map stepItem : stepItems){
                        if(isDayItemTask(stepItem)){
                            dayItemInstanceIds.add(Long.valueOf(stepItem.get("id").toString()));
                        }
                    }
                }
            }
        }
        if(dayItemInstanceIds.size() == 0 ){
            return new HashMap<>();
        }
        List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectBatchIds(dayItemInstanceIds);
        HashMap<Long, TreatmentDayitemInstanceDO.StatusEnum> result = new HashMap()   ;
        for (TreatmentDayitemInstanceDO dayitemInstanceDO : dayitemInstanceDOS) {
            result.put(dayitemInstanceDO.getId(),
                    TreatmentDayitemInstanceDO.StatusEnum.
                            fromValue(dayitemInstanceDO.getStatus()));
        }
        return result;
    }
}
