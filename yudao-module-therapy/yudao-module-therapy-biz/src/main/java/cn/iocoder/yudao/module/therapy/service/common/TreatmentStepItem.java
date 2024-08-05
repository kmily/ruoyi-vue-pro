package cn.iocoder.yudao.module.therapy.service.common;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import lombok.Data;

import java.util.List;

@Data
public class TreatmentStepItem {
    public enum ProcessStatus {
        IS_NEXT,  // 下一步
        CURRENT_DAY_NO_MORE_STEP, // 当前疗程日没有更多步骤
        LAST_DAY_NOT_COMPLETE, // 上一天存在治疗任务没有完成
        CURRENT_STEP_REQUIRES_COMPLETE, // 当前步骤需要完成,请先完成当前步骤
        TREATMENT_FINISHED, // 疗程已经结束
        TODAY_IS_BREAK_DAY, // 今天是休息日
        NOT_NEXT, // 无效
    }


    private List<TreatmentDayitemInstanceDO> day_items; // 当前组的所有步骤
    private TreatmentDayInstanceDO day; // 当前疗程日
    private TreatmentInstanceDO flowInstance; // 当前流程实例
    private boolean isStarted = true;  // 用户是否已经开始整体的流程
    private boolean isEnd = false;  // 用户是否已经结束整体的流程
    private long Agroup = -1; // 当前组的序号
    private ProcessStatus processStatus = ProcessStatus.IS_NEXT;
    private TreatmentDayitemInstanceDO lastReturnedDayitemInstanceDO;

    // For DayTaskEngine usage only
    private TreatmentFlowDayDO flowDayDO;
    private List<TreatmentFlowDayitemDO> flowDayitemDOs;
    private List<TreatmentDayitemInstanceDO> flowDayitemInstanceDOs;
    private TreatmentFlowDO treatmentFlowDO;

    public static TreatmentStepItem clone(TreatmentStepItem item){
        TreatmentStepItem newItem = new TreatmentStepItem();
        newItem.setDay_items(item.getDay_items());
        newItem.setFlowDayDO(item.getFlowDayDO());
        newItem.setDay(item.getDay());
        newItem.setFlowInstance(item.getFlowInstance());
        newItem.setTreatmentFlowDO(item.getTreatmentFlowDO());
        newItem.setProcessStatus(item.getProcessStatus());
        newItem.setFlowDayitemDOs(item.getFlowDayitemDOs());
        newItem.setEnd(item.isEnd());
        newItem.setStarted(item.isStarted());
        newItem.setAgroup(item.getAgroup());
        return newItem;
    }
}
