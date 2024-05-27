package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TreatmentUserProgressServiceImpl implements  TreatmentUserProgressService{

    @Resource
    TreatmentUserProgressMapper treatmentUserProgressMapper;

    @Resource
    TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    /**
     * 获取用户当前的任务进度
     * @param userId
     * @param treatmentInstanceId
     * @return
     */
    @Override
    public TreatmentStepItem getTreatmentUserProgress(Long userId, Long treatmentInstanceId) {
        TreatmentStepItem stepItem = new TreatmentStepItem();
        TreatmentInstanceDO instance = treatmentInstanceMapper.selectById(treatmentInstanceId);
        TreatmentUserProgressDO progressDO =  treatmentUserProgressMapper.getUserCurrentProgress(userId, treatmentInstanceId);
        stepItem.setFlowInstance(instance);
        if(progressDO != null){
            TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectById(progressDO.getDayInstanceId());
            stepItem.setDay(dayInstanceDO);

            List<TreatmentFlowDayitemDO> flowDayitemDOS = treatmentFlowDayitemMapper.selectGroupItems(dayInstanceDO.getDayId(), progressDO.getDayAgroup());
            List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectCurrentItems(flowDayitemDOS);
            stepItem.setAgroup(progressDO.getDayAgroup());
            stepItem.setDay_items(dayitemInstanceDOS);
        }else{
            stepItem.setStarted(false);
        }
        return stepItem;
    }

    public Map<String, Object> convertDayitemInstanceToMap(TreatmentDayitemInstanceDO dayitemInstanceDO) {
        HashMap<String, Object> data = new HashMap<>();
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        data.put("id", dayitemInstanceDO.getId());
        data.put("required", flowDayitemDO.isRequired());
        data.put("item_type", flowDayitemDO.getItemType());
        data.put("settings", flowDayitemDO.getSettingsObj());
        return data;
    }
    @Override
    public Map<String, Object> convertStepItemToRespFormat(TreatmentStepItem stepItem) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("process_status", stepItem.getProcessStatus());
        HashMap sysInfo = new HashMap<String, String>();
        sysInfo.put("type", "SYS_INFO");
        switch (stepItem.getProcessStatus()){
            case IS_NEXT:
                break;
            case CURRENT_DAY_NO_MORE_STEP:
                sysInfo.put("content", "今天已经没有更多任务了哦~");
                data.put("step_item", sysInfo);
                data.put("step_item_type", "SINGLE");
                return data;
            case LAST_DAY_NOT_COMPLETE:
                sysInfo.put("content", "请先完成之前的必做任务哦~");
                data.put("step_item", sysInfo);
                data.put("step_item_type", "SINGLE");
                return data;
            case CURRENT_STEP_REQUIRES_COMPLETE:
                sysInfo.put("content", "请先完成当前任务才能继续哦~");
                data.put("step_item", sysInfo);
                data.put("step_item_type", "SINGLE");
                return data;
        }
        if(stepItem.getDay_items().size() > 1){
            data.put("step_item_type", "LIST");
            List<Map<String, Object>> respItems = new ArrayList<>();
            for(TreatmentDayitemInstanceDO dayitemInstanceDO : stepItem.getDay_items()){
                Map<String, Object> respItem = convertDayitemInstanceToMap(dayitemInstanceDO);
                respItems.add(respItem);
            }
            data.put("step_item", respItems);
        }else{
            data.put("step_item_type", "SINGLE");
            Map<String, Object> respItem = convertDayitemInstanceToMap(stepItem.getDay_items().get(0));
            data.put("step_item", respItem);
        }
        return data;
    }

    @Override
    public void updateUserProgress(TreatmentStepItem stepItem){
        TreatmentUserProgressDO progressDO = treatmentUserProgressMapper.getUserCurrentProgress(stepItem.getFlowInstance().getUserId(), stepItem.getFlowInstance().getId());
        if(progressDO == null){
            progressDO = new TreatmentUserProgressDO();
            progressDO.setUserId(stepItem.getFlowInstance().getUserId());
            progressDO.setTreatmentInstanceId(stepItem.getFlowInstance().getId());
            progressDO.setDayInstanceId(stepItem.getDay().getId());
            progressDO.setDayAgroup(stepItem.getAgroup());
            treatmentUserProgressMapper.insert(progressDO);
        }else{
            progressDO.setDayInstanceId(stepItem.getDay().getId());
            progressDO.setDayAgroup(stepItem.getAgroup());
            treatmentUserProgressMapper.updateById(progressDO);
        }
    }

}
