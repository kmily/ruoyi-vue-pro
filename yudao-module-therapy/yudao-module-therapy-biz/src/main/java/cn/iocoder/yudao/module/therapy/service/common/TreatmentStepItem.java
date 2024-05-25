package cn.iocoder.yudao.module.therapy.service.common;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import lombok.Data;

import java.util.List;

@Data
public class TreatmentStepItem {
    public enum ProcessStatus {
        IS_NEXT,
        CURRENT_DAY_NO_MORE_STEP,
        LAST_DAY_NOT_COMPLETE,
        CURRENT_STEP_REQUIRES_COMPLETE,
    }


    private List<TreatmentDayitemInstanceDO> day_items; // 当前组的所有步骤
    private TreatmentDayInstanceDO day; // 当前疗程日
    private TreatmentInstanceDO flowInstance; // 当前流程实例
    private boolean isStarted = true;  // 用户是否已经开始整体的流程
    private boolean isEnd = false;  // 用户是否已经结束整体的流程
    private int group_seq; // 当前组的序号
    private ProcessStatus processStatus = ProcessStatus.IS_NEXT;

    // For DayTaskEngine usage only
    private TreatmentFlowDayDO flowDayDO;
    private List<TreatmentFlowDayitemDO> flowDayitemDOs;
    private TreatmentFlowDO treatmentFlowDO;

    public static TreatmentStepItem clone(TreatmentStepItem item){
        TreatmentStepItem newItem = new TreatmentStepItem();
        newItem.setDay_items(item.getDay_items());
        newItem.setDay(item.getDay());
        newItem.setFlowInstance(item.getFlowInstance());
        newItem.setStarted(item.isStarted());
        newItem.setGroup_seq(item.getGroup_seq());
        return newItem;
    }
}
