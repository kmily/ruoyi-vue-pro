package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserExtDTO;
import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowPlanReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowTaskVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import cn.iocoder.yudao.module.therapy.flowengine.DayTaskEngine;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    private DayTaskEngine dayTaskEngine;

    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private TreatmentFlowDayMapper treatmentFlowDayMapper;
    @Resource
    private TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

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

    @Override
    public TreatmentStepItem getNext(TreatmentStepItem userCurrentStep) {
        dayTaskEngine.initWithCurrentStep(userCurrentStep);
        TreatmentStepItem nextStepItem = dayTaskEngine.getNextStepItem();

        return nextStepItem;
    }

    @Override
    public void completeDayitemInstance(Long userId,
                                        Long dayItemInstanceId) {
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO = treatmentDayitemInstanceMapper.selectByUserIdAndId(userId, dayItemInstanceId);
        treatmentDayitemInstanceDO.setStatus(TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue());
        treatmentDayitemInstanceMapper.updateById(treatmentDayitemInstanceDO);
    }

    public boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO) {
        MemberUserExtDTO dto = memberUserApi.getUserExtInfo(userId);
        if (dto != null) {
            dto.setAppointmentDate(reqVO.getAppointmentDate());
            dto.setAppointmentTimeRange(reqVO.getAppointmentPeriodTime());
            memberUserApi.updateMemberExtByUserId(dto);
        } else {
            memberUserApi.saveUserExtInfo(dto);
        }

        return true;
    }


    @Override
    public Long createTreatmentFlow(SaveFlowReqVO createReqVO) {
        // 插入
        TreatmentFlowDO treatmentFlow = BeanUtils.toBean(createReqVO, TreatmentFlowDO.class);
        treatmentFlow.setCode(IdUtil.fastSimpleUUID());
        treatmentFlow.setStatus(CommonStatusEnum.ENABLE.getStatus());
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

    @Override
    public Long createPlanTask(FlowTaskVO vo) {
        TreatmentFlowDayDO dayDO = treatmentFlowDayMapper.selectById(vo.getDayId());
        if (Objects.isNull(dayDO)) {
            throw exception(TREATMENT_PLAN_NOT_EXISTS);
        }
        TreatmentFlowDayitemDO dayitemDO = BeanUtils.toBean(vo, TreatmentFlowDayitemDO.class);
        dayitemDO.setFlowId(dayDO.getFlowId());
        treatmentFlowDayitemMapper.insert(dayitemDO);
        return dayitemDO.getId();
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
}
