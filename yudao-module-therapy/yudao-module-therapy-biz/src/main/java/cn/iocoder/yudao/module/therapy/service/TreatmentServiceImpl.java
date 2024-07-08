package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserExtDTO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.StepItemVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.controller.vo.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowPlanReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowTaskVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;

    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private TreatmentFlowDayMapper treatmentFlowDayMapper;
    @Resource
    private TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Resource
    private TaskFlowService taskFlowService;

    @Resource
    private TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    private TTMainInsertedStepMapper tTMainInsertedStepMapper;

    @Override
    public Long initTreatmentInstance(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        TreatmentInstanceDO instance = treatmentInstanceMapper.initInstance(userId, treatmentFlow);
        return instance.getId();
    }

    @Override
    public Long getCurrentTreatmentInstance(Long userId, String treatmentCode) {
        TreatmentFlowDO treatmentFlow = treatmentFlowMapper.selectByCode(treatmentCode);
        List<TreatmentInstanceDO> instances = treatmentInstanceMapper.filterCurrentByUserAndTreatment(userId, treatmentFlow.getId());
        if (instances.size() > 0) {
            return instances.get(0).getId();
        } else {
            return -1L;
        }
    }

//    @Override
//    public TreatmentStepItem getNext(TreatmentStepItem userCurrentStep) {
//        dayTaskEngine.initWithCurrentStep(userCurrentStep);
//        TreatmentStepItem nextStepItem = dayTaskEngine.getNextStepItem();
//        return nextStepItem;
//    }

//    @Override
//    public void completeDayitemInstance(Long userId,
//                                        Long dayItemInstanceId) {
//        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
//        treatmentDayitemInstanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
//        treatmentDayitemInstanceMapper.updateById(treatmentDayitemInstanceDO);
//    }

    public boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO) {
        MemberUserExtDTO dto=new MemberUserExtDTO();
        dto.setUserId(userId);
        dto.setAppointmentDate(reqVO.getAppointmentDate());
        dto.setAppointmentTimeRange(reqVO.getAppointmentTimeRange());
        memberUserApi.updateMemberExtByUserId(dto);

        return true;
    }


    @Override
    public Long createTreatmentFlow(SaveFlowReqVO createReqVO) {
        // 插入
        TreatmentFlowDO treatmentFlow = BeanUtils.toBean(createReqVO, TreatmentFlowDO.class);
        treatmentFlow.setCode(IdUtil.fastSimpleUUID());
        treatmentFlow.setStatus(CommonStatusEnum.DISABLE.getStatus());
        treatmentFlowMapper.insert(treatmentFlow);
        // 返回
        return treatmentFlow.getId();
    }

    @Override
    public void updateTreatmentFlow(SaveFlowReqVO updateReqVO) {
        // 校验存在
        validateTreatmentFlowExists(updateReqVO.getId());
        //todo:验证什么时候允许更新

        TreatmentFlowDO updateObj = BeanUtils.toBean(updateReqVO, TreatmentFlowDO.class);
        treatmentFlowMapper.updateById(updateObj);
    }

    private void validateTreatmentFlowExists(Long id) {
        if (treatmentFlowMapper.selectById(id) == null) {
            throw exception(TREATMENT_FLOW_NOT_EXISTS);
        }
    }


    @Override
    public TreatmentFlowDO getTreatmentFlow(Long id) {
        return treatmentFlowMapper.selectById(id);
    }

    @Override
    public PageResult<TreatmentFlowDO> getTreatmentFlowPage(PageParam pageReqVO) {
        return treatmentFlowMapper.selectPage(pageReqVO);

    }

    @Override
    public Long addPlan(FlowPlanReqVO reqVO) {
        List<TreatmentFlowDayDO> list = treatmentFlowDayMapper.getPlanListByFlowId(reqVO.getFlowId());
        if (list.size() > 0 && list.stream().filter(p -> p.getSequence().equals(reqVO.getSequence())).count() > 0) {
            throw exception(TREATMENT_PLAN_SEQ_EXISTS);
        }
        TreatmentFlowDayDO dayDO = BeanUtils.toBean(reqVO, TreatmentFlowDayDO.class);
        treatmentFlowDayMapper.insert(dayDO);
        return dayDO.getId();
    }

    @Override
    public void updatePlan(FlowPlanReqVO reqVO) {
        TreatmentFlowDayDO dayDO = treatmentFlowDayMapper.selectById(reqVO.getId());
        if (Objects.isNull(dayDO)) {
            throw exception(TREATMENT_PLAN_NOT_EXISTS);
        }
        List<TreatmentFlowDayDO> list = treatmentFlowDayMapper.getPlanListByFlowId(reqVO.getFlowId());
        if (list.stream().filter(p -> !p.getId().equals(reqVO.getId()) && p.getSequence().equals(reqVO.getSequence())).count() > 0) {
            throw exception(TREATMENT_PLAN_SEQ_EXISTS);
        }
        TreatmentFlowDayDO up = BeanUtils.toBean(reqVO, TreatmentFlowDayDO.class);
        treatmentFlowDayMapper.updateById(up);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delPlan(Long id) {
        TreatmentFlowDayDO dayDO = treatmentFlowDayMapper.selectById(id);
        if (Objects.isNull(dayDO)) {
            throw exception(TREATMENT_PLAN_NOT_EXISTS);
        }
        treatmentFlowDayMapper.deleteById(id);
        treatmentFlowDayitemMapper.deleteByDayId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createPlanTask(FlowTaskVO vo) {
        TreatmentFlowDayDO dayDO = treatmentFlowDayMapper.selectById(vo.getDayId());
        if (Objects.isNull(dayDO)) {
            throw exception(TREATMENT_PLAN_NOT_EXISTS);
        }
        TreatmentFlowDayitemDO dayitemDO = BeanUtils.toBean(vo, TreatmentFlowDayitemDO.class);
        dayitemDO.setFlowId(dayDO.getFlowId());
        if (Objects.isNull(vo.getBeforeId()) || vo.getBeforeId() <= 0L) {
            dayitemDO.setAgroup(1);
            treatmentFlowDayitemMapper.insert(dayitemDO);
            taskFlowService.updateFlowFromDayitem(dayitemDO, "create");
            return dayitemDO.getId();
        } else {
            List<TreatmentFlowDayitemDO> items = getTaskListByDayId(vo.getDayId()).stream()
                    .sorted(Comparator.comparing(TreatmentFlowDayitemDO::getAgroup)).collect(Collectors.toList());
            Integer agroup = 0;
            for (TreatmentFlowDayitemDO item : items) {
                if (item.getId().equals(vo.getBeforeId())) {
                    agroup = item.getAgroup();
                    dayitemDO.setAgroup(item.getAgroup());
                }
                if (agroup > 0) {
                    item.setAgroup(++agroup);
                    treatmentFlowDayitemMapper.updateById(item);
                }
            }
            treatmentFlowDayitemMapper.insert(dayitemDO);
            // create workflow
            taskFlowService.updateFlowFromDayitem(dayitemDO, "create");
            return dayitemDO.getId();
        }

    }

    @Override
    public void updatePlanTask(FlowTaskVO vo) {
        TreatmentFlowDayDO dayDO = treatmentFlowDayMapper.selectById(vo.getDayId());
        if (Objects.isNull(dayDO)) {
            throw exception(TREATMENT_PLAN_NOT_EXISTS);
        }
        TreatmentFlowDayitemDO old = treatmentFlowDayitemMapper.selectById(vo.getId());
        if (Objects.isNull(old)) {
            throw exception(TREATMENT_PLAN_TASK_NOT_EXISTS);
        }
        TreatmentFlowDayitemDO dayitemDO = BeanUtils.toBean(vo, TreatmentFlowDayitemDO.class);
        treatmentFlowDayitemMapper.updateById(dayitemDO);
        // update workflow
        taskFlowService.updateFlowFromDayitem(dayitemDO, "update");
    }

    @Override
    public void delPlanTask(Long id) {
        TreatmentFlowDayitemDO old = treatmentFlowDayitemMapper.selectById(id);
        if (Objects.isNull(old)) {
            throw exception(TREATMENT_PLAN_TASK_NOT_EXISTS);
        }
        treatmentFlowDayitemMapper.deleteById(id);
    }

    @Override
    public List<TreatmentFlowDayDO> getPlanListByFlowId(Long id) {
        return treatmentFlowDayMapper.getPlanListByFlowId(id);
    }

    @Override
    public List<TreatmentFlowDayitemDO> getTaskListByDayId(Long id) {
        return treatmentFlowDayitemMapper.getListByDayId(id);
    }

    @Override
    public TreatmentFlowDayitemDO getTask(Long id) {
        return treatmentFlowDayitemMapper.selectById(id);
    }

    @Override
    public void publish(Long id,Integer state) {
        TreatmentFlowDO dayitemDO=treatmentFlowMapper.selectById(id);
        if(Objects.isNull(dayitemDO)){
            throw exception(TREATMENT_FLOW_NOT_EXISTS);
        }
        dayitemDO.setStatus(state);
        treatmentFlowMapper.updateById(dayitemDO);
    }

    @Override
    public void publishFlow(Long flowId){
        List<TreatmentFlowDayitemDO> list = treatmentFlowDayitemMapper.getListByFlowId(flowId);
        for (TreatmentFlowDayitemDO flowDayitemDO: list){
            taskFlowService.updateFlowFromDayitem(flowDayitemDO, "publish");
        }
    }




    /**
     * 如果当天为休息日，则完成
     * 如果当天所有必做任务都已经完成，则完成
     * @param dayInstanceDO 疗程日实例
     */
    private boolean isCompletedDayInstance(TreatmentDayInstanceDO dayInstanceDO, boolean isSameDay){
        TreatmentFlowDayDO flowDayDO = treatmentFlowDayMapper.selectById(dayInstanceDO.getDayId());
        if(flowDayDO.isHasBreak()){
            dayInstanceDO.setStatus(TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue());
            treatmentDayInstanceMapper.updateById(dayInstanceDO);
            return true;
        }
        List<TreatmentFlowDayitemDO> flowDayitemDOS;
        if(isSameDay){
            flowDayitemDOS = treatmentFlowDayitemMapper.queryTasks(flowDayDO.getId());
        }else{
            flowDayitemDOS = treatmentFlowDayitemMapper.queryRequiredTasks(flowDayDO.getId());
        }
        if(flowDayitemDOS.size() == 0){
            return true;
        }
        List<TreatmentDayitemInstanceDO> treatmentDayitemInstanceDOS = treatmentDayitemInstanceMapper.getUserDayitemInstances(
                dayInstanceDO.getUserId(),
                dayInstanceDO.getFlowInstanceId(),
                flowDayitemDOS
        );
        if(treatmentDayitemInstanceDOS.size() < flowDayitemDOS.size()){
            return false;
        }
        boolean completed = true;
        for(TreatmentDayitemInstanceDO dayitemInstanceDO : treatmentDayitemInstanceDOS){
            if(dayitemInstanceDO.getStatus() != TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue()){
                completed = false;
            }
        }
        return completed;
    }


    /**
     * 更新疗程日的状态
     * 如果当天为休息日，则完成
     * 如果当天所有必做任务都已经完成，则完成
     * @param dayInstanceDO 疗程日实例
     */
    @Override
    public void updateDayInstanceStatus(TreatmentDayInstanceDO dayInstanceDO, boolean isSameDay){
        boolean completed = isCompletedDayInstance(dayInstanceDO, isSameDay);
        if(completed){
            dayInstanceDO.setStatus(TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue());
            treatmentDayInstanceMapper.updateById(dayInstanceDO);
            updateTreatmentInstanceStatus(dayInstanceDO, isSameDay);
        }
    }


    /**
     * 更新疗程实例的状态
     * @param dayInstanceDO
     */
    private void updateTreatmentInstanceStatus(TreatmentDayInstanceDO dayInstanceDO, boolean isSameDay){
        TreatmentFlowDayDO flowDayDO = treatmentFlowDayMapper.selectById(dayInstanceDO.getDayId());
        if(!treatmentFlowDayMapper.isLastFlowDay(flowDayDO)){
            return;
        }
        if(isCompletedDayInstance(dayInstanceDO, isSameDay)){
            TreatmentInstanceDO instanceDO = treatmentInstanceMapper.selectById(dayInstanceDO.getFlowInstanceId());
            instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.COMPLETED.getValue());
            treatmentInstanceMapper.updateById(instanceDO);
        }
    }

    @Override
    public void finishDayItemInstance(Long dayItemInstanceId){
        treatmentDayitemInstanceMapper.finishDayItemInstanceDO(dayItemInstanceId);
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        TreatmentDayInstanceDO dayInstanceDO = treatmentDayInstanceMapper.selectById(dayitemInstanceDO.getDayInstanceId());
        updateDayInstanceStatus(dayInstanceDO, true);
        updateTreatmentInstanceStatus(dayInstanceDO, true);
    }

    @Override
    public void cancelTreatmentInstance(Long flowInstanceId){
        TreatmentInstanceDO instanceDO = treatmentInstanceMapper.selectById(flowInstanceId);
        instanceDO.setStatus(TreatmentInstanceDO.TreatmentStatus.CANCELLED.getValue());
        treatmentInstanceMapper.updateById(instanceDO);
    }

//    @Override
//    public void completeDayInstance(TreatmentDayInstanceDO dayInstanceDO){
//        dayInstanceDO.setStatus(TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue());
//        treatmentDayInstanceMapper.updateById(dayInstanceDO);
//        updateTreatmentInstanceStatus(dayInstanceDO);
//    }
    @Override
    public TreatmentNextVO getInsertedNextVO(Long userId, Long treatmentInstanceId){
        List<TTMainInsertedStepDO> data = tTMainInsertedStepMapper.getInsertedNextVO(userId, treatmentInstanceId);
        if(data.size() == 0){
            return null;
        }else{
            TTMainInsertedStepDO stepDO = data.get(0);
            Map<String, Object> stepData = stepDO.getMessageObj();
            TreatmentNextVO vo = new TreatmentNextVO();
            StepItemVO stepItemVO = new StepItemVO();
            stepItemVO.setItem_type(stepData.get("item_type").toString());
            stepItemVO.setRequired(false);
            stepItemVO.setSettings((Map) stepData.get("settings"));
            vo.setStep_item(stepItemVO);
            stepDO.setStatus(TTMainInsertedStepDO.StatusEnum.USED.getValue());
            tTMainInsertedStepMapper.updateById(stepDO);
            return vo;
        }
    }


    public void addGuideLanguageStep(Long userId, Long treatmentInstanceId, String content){
        Long maxSeq = tTMainInsertedStepMapper.selectCount(new LambdaQueryWrapper<TTMainInsertedStepDO>()
                .eq(TTMainInsertedStepDO::getUserId, userId)
                .eq(TTMainInsertedStepDO::getTreatmentInstanceId, treatmentInstanceId)
        );
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();
        TTMainInsertedStepDO insertedStepDO = new TTMainInsertedStepDO();
        insertedStepDO.setUserId(userId);
        insertedStepDO.setTreatmentInstanceId(treatmentInstanceId);
        insertedStepDO.setSequence(maxSeq.intValue() + 1);
        insertedStepDO.setStatus(TTMainInsertedStepDO.StatusEnum.DEFAULT.getValue());
        HashMap<String, Object> message = new HashMap<String, Object>();
        HashMap<String, Object> settingsMap = new HashMap<>();
        settingsMap.put("content", content);
        message.put("item_type", TaskType.GUIDE_LANGUAGE.getCode());
        message.put("settings", settingsMap);
        try {
            insertedStepDO.setMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        tTMainInsertedStepMapper.insert(insertedStepDO);
    }
}
