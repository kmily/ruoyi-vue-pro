package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.StepItemVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import org.springframework.stereotype.Service;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if(instance.getStatus() == TreatmentInstanceDO.TreatmentStatus.CANCELLED.getValue() ||
                instance.getStatus() == TreatmentInstanceDO.TreatmentStatus.COMPLETED.getValue()){
            stepItem.setEnd(true);
            return stepItem;
        }
        stepItem.setFlowInstance(instance);
        if(progressDO != null){
            TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectById(progressDO.getDayInstanceId());
            stepItem.setDay(dayInstanceDO);

            List<TreatmentFlowDayitemDO> flowDayitemDOS = treatmentFlowDayitemMapper.selectGroupItems(dayInstanceDO.getDayId(), progressDO.getDayAgroup());
            if(flowDayitemDOS.size() > 0){
                List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectCurrentItems(flowDayitemDOS);
                stepItem.setAgroup(progressDO.getDayAgroup());
                stepItem.setDay_items(dayitemInstanceDOS);
            }
        }else{
            stepItem.setStarted(false);
        }
        return stepItem;
    }

    public StepItemVO convertDayitemInstanceToMap(TreatmentDayitemInstanceDO dayitemInstanceDO, String userName) {
        StepItemVO data = new StepItemVO();
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.selectById(dayitemInstanceDO.getDayitemId());
        data.setId(dayitemInstanceDO.getId());
        data.setRequired(flowDayitemDO.isRequired());
        data.setItem_type(flowDayitemDO.getItemType());
        data.setDayitem_id(dayitemInstanceDO.getDayitemId());
        data.setSettings(flowDayitemDO.getSettingsObj());
        if(flowDayitemDO.getItemType().equals(TaskType.GUIDE_LANGUAGE.getCode())){
            String content = ((String) flowDayitemDO.getSettingsObj().getOrDefault("text", "")).replace("用户昵称", userName);
            data.getSettings().put("content", content);
        }
        return data;
    }

    private StepItemVO generateSystemInfoVO(TreatmentStepItem stepItem){
        StepItemVO sysInfo = new StepItemVO();
        HashMap settings = new HashMap<String, String>();
        sysInfo.setItem_type("SYS_INFO");
        sysInfo.setRequired(false);
        switch (stepItem.getProcessStatus()){
            case CURRENT_DAY_NO_MORE_STEP:
                settings.put("content", "今天已经没有更多任务了哦~");
                break;
            case LAST_DAY_NOT_COMPLETE:
                settings.put("content", "请先完成之前的必做任务哦~");
                break;
            case CURRENT_STEP_REQUIRES_COMPLETE:
                settings.put("content", "请先完成当前任务才能继续哦~");
                break;
            case TREATMENT_FINISHED:
                settings.put("content", "恭喜你完成了所有任务,治疗流程已经结束~");
                break;
            case TODAY_IS_BREAK_DAY:
                settings.put("content", "今天是休息日哦~");
                break;
        }
        sysInfo.setSettings(settings);
        return sysInfo;
    }

    @Override
    public TreatmentNextVO convertStepItemToRespFormat(TreatmentStepItem stepItem, String userName) {
        TreatmentNextVO data = new TreatmentNextVO();
        data.setProcess_status(stepItem.getProcessStatus().toString());
        if(!stepItem.getProcessStatus().equals(TreatmentStepItem.ProcessStatus.IS_NEXT)){
            StepItemVO sysInfoVO = generateSystemInfoVO(stepItem);
            data.setStep_item_type("SINGLE");
            data.setStep_item(sysInfoVO);
        }else{
            if(stepItem.getDay_items().size() > 1){
                data.setStep_item_type("LIST");
                List<StepItemVO> stepItemVOS = new ArrayList<>();
                for(TreatmentDayitemInstanceDO dayitemInstanceDO : stepItem.getDay_items()){
                    StepItemVO respItem = convertDayitemInstanceToMap(dayitemInstanceDO, userName);
                    stepItemVOS.add(respItem);
                }
                data.setStep_items(stepItemVOS);
            }else{
                data.setStep_item_type("SINGLE");
                StepItemVO stepItemVO = convertDayitemInstanceToMap(stepItem.getDay_items().get(0), userName);
                data.setStep_item(stepItemVO);
            }
        }
        return data;
    }

    @Override
    public void updateUserProgress(TreatmentStepItem stepItem){
        TreatmentUserProgressDO progressDO = treatmentUserProgressMapper.getUserCurrentProgress(stepItem.getFlowInstance().getUserId(), stepItem.getFlowInstance().getId());
        if(stepItem.getProcessStatus() == TreatmentStepItem.ProcessStatus.TREATMENT_FINISHED) {
            return;
        }
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

    @Override
    public void clearUserProgress(Long userId){
        treatmentUserProgressMapper.delete(TreatmentUserProgressDO::getUserId, userId);
        treatmentDayInstanceMapper.delete(TreatmentDayInstanceDO::getUserId, userId);
        treatmentDayitemInstanceMapper.delete(TreatmentDayitemInstanceDO::getUserId, userId);
        treatmentInstanceMapper.delete(TreatmentInstanceDO::getUserId, userId);
    }

    @Override
    public void endTreatment(Long userId){
        treatmentInstanceMapper.endTreatment(userId);
    }


}
