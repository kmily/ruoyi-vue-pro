package cn.iocoder.yudao.module.therapy.flowengine;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DayTaskEngine {

    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    private TreatmentFlowDayMapper treatmentFlowDayMapper;

    @Resource
    private TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    private TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;


    private TreatmentStepItem userCurrentStep;

    private TreatmentService treatmentService;

    /**
     * 构造函数
     * @param userCurrentStep 用户当前的步骤
     */
    public DayTaskEngine initWithCurrentStep(TreatmentStepItem userCurrentStep) {
        this.userCurrentStep = userCurrentStep;
        if(userCurrentStep.isStarted()){
            prepareUserCurrentStep();
        }
        return this;
    }

    /**
     * 补充userCurrentStep的数据，增加每个instance所对应的流程模板
     */
    private void prepareUserCurrentStep(){
        TreatmentFlowDO flowDO = treatmentFlowMapper.selectById(userCurrentStep.getFlowInstance().getFlowId());
        TreatmentFlowDayDO flowDayDO = treatmentFlowDayMapper.selectById(userCurrentStep.getDay().getDayId());
        if(!flowDayDO.isHasBreak()) {
            List<TreatmentDayitemInstanceDO> dayitemInstanceDOs = userCurrentStep.getDay_items();
            List<Long> ids = dayitemInstanceDOs.stream().map(TreatmentDayitemInstanceDO::getDayitemId).collect(Collectors.toList());
            if(ids.size() > 0){
                List<TreatmentFlowDayitemDO> flowDayitemDOs = treatmentFlowDayitemMapper.selectBatchIds(ids);
                userCurrentStep.setFlowDayitemDOs(flowDayitemDOs);
            }
        }
        userCurrentStep.setFlowDayDO(flowDayDO);
        userCurrentStep.setTreatmentFlowDO(flowDO);
    }

    /**
     * 初始实例化用户流程的第一个步骤，并且返回结果
     * @return
     */
    private TreatmentStepItem initAndGetFirstStep(){
        TreatmentInstanceDO flowInstanceDO = userCurrentStep.getFlowInstance();
        Long flowId = flowInstanceDO.getFlowId();
        TreatmentFlowDayDO firstFlowDayDO = treatmentFlowDayMapper.getFirstFlowDay(flowId);
        TreatmentDayInstanceDO dayInstanceDo = treatmentDayInstanceMapper.
                initInstance(
                        flowInstanceDO.getUserId(),
                        firstFlowDayDO.getId(),
                        flowInstanceDO.getId()
                );
        TreatmentStepItem stepItem = new TreatmentStepItem();
        stepItem.setStarted(true);
        stepItem.setFlowInstance(flowInstanceDO);
        stepItem.setDay(dayInstanceDo);
        // if not a break day
        if (!firstFlowDayDO.isHasBreak()) {
            List<TreatmentFlowDayitemDO> dayitemsDO = treatmentFlowDayitemMapper.getFirstGroupFlowDayitems(firstFlowDayDO.getId());;
            List<TreatmentDayitemInstanceDO> dayitemInstancesDO = treatmentDayitemInstanceMapper.initInstances(
                    flowInstanceDO.getUserId(),
                    dayInstanceDo,
                    dayitemsDO
            );
            stepItem.setDay_items(dayitemInstancesDO);
            stepItem.setAgroup(dayitemsDO.get(0).getAgroup());
        }else{
            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
        }
        return stepItem;
    }

    private TreatmentStepItem getBreakDayNextStepItem(){
        TreatmentDayInstanceDO dayInstanceDO = userCurrentStep.getDay();
        boolean isSameDay = dayInstanceDO.getCreateTime().getDayOfYear()!= LocalDateTime.now().getDayOfYear();
        TreatmentStepItem stepItem = TreatmentStepItem.clone(userCurrentStep);
        if(!isSameDay){
            // next day
            TreatmentFlowDayDO nextDayDO = treatmentFlowDayMapper.getNextFlowDay(userCurrentStep.getFlowDayDO());
            if(nextDayDO == null){
                // no next day
                stepItem.setEnd(true);
                return stepItem;
            }
            stepItem = getNextStepItemOfNextDay(userCurrentStep);
        }else{
            // same day
            dayInstanceDO.setStatus(TreatmentDayInstanceDO.StatusEnum.IN_PROGRESS.getValue());
            treatmentDayInstanceMapper.updateById(dayInstanceDO);
        }
        return stepItem;
    }

    /**
     * 获取下一个步骤
     * @return 下一个步骤
     */
    public TreatmentStepItem getNextStepItem(){
        if(!userCurrentStep.isStarted()){
            //用户还没有开始
            return initAndGetFirstStep();
        }
        if(userCurrentStep.isEnd()){
            TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TREATMENT_FINISHED);
            return stepItem;
        }
        if(userCurrentStep.getFlowDayDO().isHasBreak()){
            //用户当前步骤是休息日
            TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
            return stepItem;
//            return getBreakDayNextStepItem();
        }else{
            //用户当前步骤不是休息日
            TreatmentDayInstanceDO dayInstanceDO = userCurrentStep.getDay();
            boolean isSameDay = dayInstanceDO.getCreateTime().getDayOfYear() == LocalDateTime.now().getDayOfYear();
            if(!isSameDay) {
                // next day
                // Update Yesterday Status
                treatmentService.updateDayInstanceStatus(userCurrentStep.getDay()); //更新昨天的状态

                if(treatmentFlowDayitemMapper.hasNextGroup(userCurrentStep.getFlowDayDO().getId(), userCurrentStep.getAgroup())){
                    return getNextStepItemOfCurrentStepItemInSameDay(userCurrentStep);
                }else{
                    boolean yesterdayDayItemsCompleted = userCurrentStep.getDay().getStatus() == TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue();
                    TreatmentStepItem stepItem;
                    if (!yesterdayDayItemsCompleted) {
                        // yesterday is not completed
                        stepItem = getNextStepItemOfNextDay(userCurrentStep);
                        stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.LAST_DAY_NOT_COMPLETE);
                    } else {
                        // return new days task
                        stepItem = getNextStepItemOfNextDay(userCurrentStep);
                        stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.IS_NEXT);
                    }
                    return stepItem;
                }
            }else{
                //same day
                if(treatmentFlowDayitemMapper.hasNextGroup(userCurrentStep.getFlowDayDO().getId(), userCurrentStep.getAgroup())){
                    return getNextStepItemOfCurrentStepItemInSameDay(userCurrentStep);
                }else{
                    TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
                    stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.CURRENT_DAY_NO_MORE_STEP);
                    return stepItem;
                }
            }

        }
    }

    private TreatmentStepItem getNextStepItemOfCurrentStepItemInSameDay(TreatmentStepItem userCurrentStep){
        TreatmentFlowDayDO dayDO = userCurrentStep.getFlowDayDO();
        List<TreatmentFlowDayitemDO> dayitemDOs = treatmentFlowDayitemMapper.getNextGroup(dayDO.getId(), userCurrentStep.getAgroup());
        List<TreatmentDayitemInstanceDO> dayitemInstanceDOs = treatmentDayitemInstanceMapper.initInstances(
                userCurrentStep.getFlowInstance().getUserId(),
                userCurrentStep.getDay(),
                dayitemDOs
        );
        TreatmentStepItem stepItem = TreatmentStepItem.clone(userCurrentStep);
        stepItem.setDay_items(dayitemInstanceDOs);
        stepItem.setAgroup(dayitemDOs.get(0).getAgroup());
        return stepItem;
    }

    private TreatmentStepItem getNextStepItemOfNextDay(TreatmentStepItem userCurrentStep){
        TreatmentFlowDayDO nextDayDO = treatmentFlowDayMapper.getNextFlowDay(userCurrentStep.getFlowDayDO());
        TreatmentStepItem stepItem = TreatmentStepItem.clone(userCurrentStep);
        List<TreatmentFlowDayitemDO> dayitemDOS = treatmentFlowDayitemMapper.getFirstGroupFlowDayitems(nextDayDO.getId());;
        TreatmentDayInstanceDO nextDayInstanceDO = treatmentDayInstanceMapper.initInstance(
                userCurrentStep.getFlowInstance().getUserId(),
                nextDayDO.getId(),
                userCurrentStep.getFlowInstance().getId()
        );
        List<TreatmentDayitemInstanceDO> dayitemInstancesDO = treatmentDayitemInstanceMapper.initInstances(
                userCurrentStep.getFlowInstance().getUserId(),
                nextDayInstanceDO,
                dayitemDOS
        );
        stepItem.setDay_items(dayitemInstancesDO);
        stepItem.setAgroup(dayitemDOS.get(0).getAgroup());
        return stepItem;
    }
}
