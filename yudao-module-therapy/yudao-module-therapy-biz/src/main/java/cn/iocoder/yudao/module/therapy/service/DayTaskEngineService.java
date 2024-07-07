package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayTaskEngineService {

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

    @Resource
    private TreatmentService treatmentService;

    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    private TreatmentUserProgressMapper treatmentUserProgressMapper;


    /**
     * 构造函数
     * @param userCurrentStep 用户当前的步骤
     */
//    public DayTaskEngineService initWithCurrentStep(TreatmentStepItem userCurrentStep) {
//        this.userCurrentStep = userCurrentStep;
//        if(userCurrentStep.isStarted()){
//            prepareUserCurrentStep();
//        }
//        return this;
//    }


//    public TreatmentStepItem getNext(Long treatmentInstanceId) {
//        this.userProgressDO = userProgressDO;
//        convertUserProgressToUserCurrentStep();
//        prepareUserCurrentStep();
//        return getNextStepItemResult();
//    }

    /**
     * 补充userCurrentStep的数据，增加每个instance所对应的流程模板
     */
//    private void prepareUserCurrentStep(){
//        TreatmentFlowDO flowDO = treatmentFlowMapper.selectById(userCurrentStep.getFlowInstance().getFlowId());
//        TreatmentFlowDayDO flowDayDO = treatmentFlowDayMapper.selectById(userCurrentStep.getDay().getDayId());
//        if(!flowDayDO.isHasBreak()) {
//            List<TreatmentDayitemInstanceDO> dayitemInstanceDOs = userCurrentStep.getDay_items();
//            List<Long> ids = dayitemInstanceDOs.stream().map(TreatmentDayitemInstanceDO::getDayitemId).collect(Collectors.toList());
//            if(ids.size() > 0){
//                List<TreatmentFlowDayitemDO> flowDayitemDOs = treatmentFlowDayitemMapper.selectBatchIds(ids);
//                userCurrentStep.setFlowDayitemDOs(flowDayitemDOs);
//            }
//        }
//        userCurrentStep.setFlowDayDO(flowDayDO);
//        userCurrentStep.setTreatmentFlowDO(flowDO);
//    }

    /**
     * 初始实例化用户流程的第一个步骤，并且返回结果
     * @return
     */
//    private TreatmentStepItem initAndGetFirstStep(){
//        TreatmentInstanceDO flowInstanceDO = userCurrentStep.getFlowInstance();
//        Long flowId = flowInstanceDO.getFlowId();
//        TreatmentFlowDayDO firstFlowDayDO = treatmentFlowDayMapper.getFirstFlowDay(flowId);
//        TreatmentDayInstanceDO dayInstanceDo = treatmentDayInstanceMapper.
//                initInstance(
//                        flowInstanceDO.getUserId(),
//                        firstFlowDayDO.getId(),
//                        flowInstanceDO.getId()
//                );
//        TreatmentStepItem stepItem = new TreatmentStepItem();
//        stepItem.setStarted(true);
//        stepItem.setFlowInstance(flowInstanceDO);
//        stepItem.setDay(dayInstanceDo);
//        // if not a break day
//        if (!firstFlowDayDO.isHasBreak()) {
//            List<TreatmentFlowDayitemDO> dayitemsDO = treatmentFlowDayitemMapper.getFirstGroupFlowDayitems(firstFlowDayDO.getId());;
//            List<TreatmentDayitemInstanceDO> dayitemInstancesDO = treatmentDayitemInstanceMapper.initInstances(
//                    flowInstanceDO.getUserId(),
//                    dayInstanceDo,
//                    dayitemsDO
//            );
//            stepItem.setDay_items(dayitemInstancesDO);
//            stepItem.setAgroup(dayitemsDO.get(0).getAgroup());
//        }else{
//            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
//        }
//        return stepItem;
//    }
//
//    private TreatmentStepItem getBreakDayNextStepItem(TreatmentStepItem userCurrentStep){
//        TreatmentDayInstanceDO dayInstanceDO = userCurrentStep.getDay();
//        boolean isSameDay = dayInstanceDO.getCreateTime().getDayOfYear()== LocalDateTime.now().getDayOfYear();
//        TreatmentStepItem stepItem = TreatmentStepItem.clone(userCurrentStep);
//        if(!isSameDay){
//            // next day
//            TreatmentFlowDayDO nextDayDO = treatmentFlowDayMapper.getNextFlowDay(userCurrentStep.getFlowDayDO());
//            stepItem = getNextStepItemOfNextDay(userCurrentStep);
//        }else{
//            // same day
//            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
//            treatmentService.completeDayInstance(dayInstanceDO);
//        }
//        return stepItem;
//    }

    /**
     * 获取下一个步骤
     * @return 下一个步骤
     */
//    public TreatmentStepItem getNextStepItem(){
//        if(!userCurrentStep.isStarted()){
//            //用户还没有开始
//            return initAndGetFirstStep();
//        }
//        if(userCurrentStep.isEnd()){
//            TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
//            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TREATMENT_FINISHED);
//            return stepItem;
//        }
//        if(userCurrentStep.getFlowDayDO().isHasBreak()){
//            //用户当前步骤是休息日
//            return getBreakDayNextStepItem(userCurrentStep);
//        }else{
//            //用户当前步骤不是休息日
//            TreatmentDayInstanceDO dayInstanceDO = userCurrentStep.getDay();
//            boolean isSameDay = dayInstanceDO.getCreateTime().getDayOfYear() == LocalDateTime.now().getDayOfYear();
//            if(!isSameDay) {
//                // next day
//                // Update Yesterday Status
//                treatmentService.updateDayInstanceStatus(userCurrentStep.getDay()); //更新昨天的状态
//
//                if(treatmentFlowDayitemMapper.hasNextGroup(userCurrentStep.getFlowDayDO().getId(), userCurrentStep.getAgroup())){
//                    return getNextStepItemOfCurrentStepItemInSameDay(userCurrentStep);
//                }else{
//                    boolean yesterdayDayItemsCompleted = userCurrentStep.getDay().getStatus() == TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue();
//                    TreatmentStepItem stepItem;
//                    if (!yesterdayDayItemsCompleted) {
//                        // yesterday is not completed
//                        stepItem = getNextStepItemOfNextDay(userCurrentStep);
//                        stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.LAST_DAY_NOT_COMPLETE);
//                    } else {
//                        // return new days task
//                        stepItem = getNextStepItemOfNextDay(userCurrentStep);
//                        stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.IS_NEXT);
//                    }
//                    return stepItem;
//                }
//            }else{
//                //same day
//                if(treatmentFlowDayitemMapper.hasNextGroup(userCurrentStep.getFlowDayDO().getId(), userCurrentStep.getAgroup())){
//                    return getNextStepItemOfCurrentStepItemInSameDay(userCurrentStep);
//                }else{
//                    TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
//                    stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.CURRENT_DAY_NO_MORE_STEP);
//                    return stepItem;
//                }
//            }
//
//        }
//    }

    private TreatmentStepItem getNextStepItemOfCurrentStepItemInSameDay(TreatmentStepItem userCurrentStep){
        TreatmentFlowDayDO dayDO = userCurrentStep.getFlowDayDO();
        if(dayDO.isHasBreak()){
            userCurrentStep.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
            return userCurrentStep;
        }
        List<TreatmentFlowDayitemDO> dayitemDOs = treatmentFlowDayitemMapper.getNextGroup(dayDO.getId(), userCurrentStep.getAgroup());
        if(dayitemDOs == null){
            userCurrentStep.setProcessStatus(TreatmentStepItem.ProcessStatus.CURRENT_DAY_NO_MORE_STEP);
            return userCurrentStep;
        }
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

//    private TreatmentStepItem getNextStepItemOfNextDay(TreatmentStepItem userCurrentStep){
//        TreatmentFlowDayDO nextDayDO = treatmentFlowDayMapper.getNextFlowDay(userCurrentStep.getFlowDayDO());
//        TreatmentStepItem stepItem = TreatmentStepItem.clone(userCurrentStep);
//        if(nextDayDO == null){
//            stepItem.setEnd(true);
//            return stepItem;
//        }
//        if(nextDayDO.isHasBreak()){
//            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY);
//            return stepItem;
//        }
//        List<TreatmentFlowDayitemDO> dayitemDOS = treatmentFlowDayitemMapper.getFirstGroupFlowDayitems(nextDayDO.getId());;
//        TreatmentDayInstanceDO nextDayInstanceDO = treatmentDayInstanceMapper.initInstance(
//                userCurrentStep.getFlowInstance().getUserId(),
//                nextDayDO.getId(),
//                userCurrentStep.getFlowInstance().getId()
//        );
//        List<TreatmentDayitemInstanceDO> dayitemInstancesDO = treatmentDayitemInstanceMapper.initInstances(
//                userCurrentStep.getFlowInstance().getUserId(),
//                nextDayInstanceDO,
//                dayitemDOS
//        );
//        stepItem.setDay(nextDayInstanceDO);
//        stepItem.setDay_items(dayitemInstancesDO);
//        stepItem.setAgroup(dayitemDOS.get(0).getAgroup());
//        return stepItem;
//    }


    private TreatmentStepItem initTreatmentStepItem(TreatmentInstanceDO instanceDO){
        TreatmentStepItem stepItem = new TreatmentStepItem();
        stepItem.setStarted(false);
        stepItem.setTreatmentFlowDO(treatmentFlowMapper.selectById(instanceDO.getFlowId()));
        stepItem.setFlowInstance(instanceDO);
        return stepItem;
    }

    public TreatmentStepItem getCurrentStep(Long treatmentInstanceId){
        TreatmentInstanceDO instance = treatmentInstanceMapper.selectById(treatmentInstanceId);
        TreatmentUserProgressDO progressDO =  treatmentUserProgressMapper.getUserCurrentProgress(instance.getUserId(), treatmentInstanceId);
        if(progressDO == null){
            return initTreatmentStepItem(instance);
        }
        return convertUserProgressToUserCurrentStep(progressDO);
    }



    public TreatmentStepItem convertUserProgressToUserCurrentStep(TreatmentUserProgressDO progressDO){
        TreatmentStepItem stepItem = new TreatmentStepItem();
        TreatmentInstanceDO instance = treatmentInstanceMapper.selectById(progressDO.getTreatmentInstanceId());
        stepItem.setFlowInstance(instance);
        if(instance.getStatus() == TreatmentInstanceDO.TreatmentStatus.CANCELLED.getValue() ||
                instance.getStatus() == TreatmentInstanceDO.TreatmentStatus.COMPLETED.getValue()){
            stepItem.setEnd(true);
            return stepItem;
        }
        TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectById(progressDO.getDayInstanceId());
        stepItem.setDay(dayInstanceDO);
        List<TreatmentFlowDayitemDO> flowDayitemDOS = treatmentFlowDayitemMapper.selectGroupItems(dayInstanceDO.getDayId(), progressDO.getDayAgroup());
        if(flowDayitemDOS.size() > 0){
            List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectCurrentItems(flowDayitemDOS);
            stepItem.setAgroup(progressDO.getDayAgroup());
            stepItem.setDay_items(dayitemInstanceDOS);
        }

        TreatmentFlowDO flowDO = treatmentFlowMapper.selectById(stepItem.getFlowInstance().getFlowId());
        TreatmentFlowDayDO flowDayDO = treatmentFlowDayMapper.selectById(stepItem.getDay().getDayId());
        if(!flowDayDO.isHasBreak()) {
            List<TreatmentDayitemInstanceDO> dayitemInstanceDOs = stepItem.getDay_items();
            List<Long> ids = dayitemInstanceDOs.stream().map(TreatmentDayitemInstanceDO::getDayitemId).collect(Collectors.toList());
            if(ids.size() > 0){
                List<TreatmentFlowDayitemDO> flowDayitemDOs = treatmentFlowDayitemMapper.selectBatchIds(ids);
                stepItem.setFlowDayitemDOs(flowDayitemDOs);
            }
        }
        stepItem.setFlowDayDO(flowDayDO);
        stepItem.setTreatmentFlowDO(flowDO);

        return stepItem;
    }

    private TreatmentStepItem initFirstDayInstance(TreatmentStepItem userCurrentStep){
        TreatmentInstanceDO flowInstanceDO = userCurrentStep.getFlowInstance();
        Long flowId = flowInstanceDO.getFlowId();
        TreatmentFlowDayDO firstFlowDayDO = treatmentFlowDayMapper.getFirstFlowDay(flowId);
        TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectOne(
                new LambdaQueryWrapperX<TreatmentDayInstanceDO>()
                        .eq(TreatmentDayInstanceDO::getDayId, firstFlowDayDO.getId())
                        .eq(TreatmentDayInstanceDO::getUserId, flowInstanceDO.getUserId())
                        .eq(TreatmentDayInstanceDO::getFlowInstanceId, flowInstanceDO.getId())
        );
        userCurrentStep.setDay(dayInstanceDO);
        userCurrentStep.setFlowDayDO(firstFlowDayDO);
        if (dayInstanceDO == null) {
            dayInstanceDO = treatmentDayInstanceMapper.
                    initInstance(
                            flowInstanceDO.getUserId(),
                            firstFlowDayDO.getId(),
                            flowInstanceDO.getId()
                    );
        }
        userCurrentStep.setDay(dayInstanceDO);
        return userCurrentStep;
    }

    private TreatmentStepItem jumpToNextDay(TreatmentStepItem userCurrentStep){
        TreatmentFlowDayDO nextDayDO = treatmentFlowDayMapper.getNextFlowDay(userCurrentStep.getFlowDayDO());
        if(nextDayDO == null){
            userCurrentStep.setEnd(true);
            return userCurrentStep;
        }
        TreatmentDayInstanceDO nextDayInstanceDO = treatmentDayInstanceMapper.initInstance(
                userCurrentStep.getFlowInstance().getUserId(),
                nextDayDO.getId(),
                userCurrentStep.getFlowInstance().getId()
        );
        userCurrentStep.setFlowDayDO(nextDayDO);
        userCurrentStep.setDay(nextDayInstanceDO);
        userCurrentStep.setAgroup(-1L);
        userCurrentStep.setDay_items(null);
        userCurrentStep.setProcessStatus(TreatmentStepItem.ProcessStatus.IS_NEXT);
        return userCurrentStep;
    }

    /**
     * 获取下一个步骤
     * @return 下一个步骤
     */
    public TreatmentStepItem getNextStepItemResult(TreatmentStepItem userCurrentStep, boolean isFreeStyle){

        if(userCurrentStep.isEnd()){
            TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
            stepItem.setProcessStatus(TreatmentStepItem.ProcessStatus.TREATMENT_FINISHED);
            return stepItem;
        }


        // Treatment Level


        // Day Level
//        if(userCurrentStep.isStarted()){
//            userCurrentStep = initFirstDayInstance(userCurrentStep);
//        }
        TreatmentDayInstanceDO dayInstanceDO = userCurrentStep.getDay();
        int maxWholeDays = 1;
        int maxSpanDays = 1;

        if(userCurrentStep.isStarted()){ // 已经完成过初始化
            maxSpanDays = LocalDateTime.now().getDayOfYear() -  dayInstanceDO.getCreateTime().getDayOfYear() + 1;
        }
        if(!isFreeStyle){//TODO BUG
            maxWholeDays = 1000;
            maxSpanDays = 1000;
        }
        if(userCurrentStep.isStarted()){
            boolean isSameDay = maxSpanDays == 1;
            treatmentService.updateDayInstanceStatus(userCurrentStep.getDay(), isSameDay); //更新天的状态
        }

        TreatmentStepItem stepItem =  TreatmentStepItem.clone(userCurrentStep);
        while (maxWholeDays > 0 && maxSpanDays > 0){
            if(!stepItem.isStarted()){
                stepItem = initFirstDayInstance(stepItem);
                return getNextStepItemOfCurrentStepItemInSameDay(stepItem);
            }
            maxSpanDays -= 1;
            if(stepItem.getDay().getStatus() == TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue() && maxSpanDays > 0){
                stepItem = jumpToNextDay(stepItem);
                stepItem = getNextStepItemOfCurrentStepItemInSameDay(stepItem);
                if(!stepItem.getProcessStatus().equals(TreatmentStepItem.ProcessStatus.TODAY_IS_BREAK_DAY)){
                    maxWholeDays -= 1;
                }
            }else{
                stepItem = getNextStepItemOfCurrentStepItemInSameDay(stepItem);
            }
            if(stepItem.getDay_items()!= null){
                return stepItem;
            }
        }
        return stepItem;
    }
}

/***
 *
 * UserProgress userId lastTime DayInstanceDO DayItemGroup
 *
 * 治疗流程层级
 *      跟新流程状态（当前日期和UserProgress）
 *      是否结束
 *      是否放弃
 *      是否初始化
 *          初始化流程实例
 *      **日期层级
 *          是否初始化
 *              初始化日期实例
 *          更新日期实例状态
 *          当前日期和UserProgress差值（完整任务天数，最多一天。跨度天数不能超过）
 *              0，继续当天任务
 *              1，完成后可以进入下一天
 *              2. 完成后进入下一天
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */