package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.controller.vo.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowPlanReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowTaskVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import javax.validation.Valid;
import java.util.List;


public interface TreatmentService {
    Long initTreatmentInstance(Long userId, String treatmentCode);

    Long getCurrentTreatmentInstance(Long userId, String treatmentCode);

//    TreatmentStepItem getNext(TreatmentStepItem userCurrentStep);

//    void completeDayitemInstance(Long userId, Long dayItemInstanceId);

    boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO);

    /**
     * 创建治疗流程模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTreatmentFlow(@Valid SaveFlowReqVO createReqVO);

    /**
     * 更新治疗流程模板
     *
     * @param updateReqVO 更新信息
     */
    void updateTreatmentFlow(@Valid SaveFlowReqVO updateReqVO);


    /**
     * 获得治疗流程模板
     *
     * @param id 编号
     * @return 治疗流程模板
     */
    TreatmentFlowDO getTreatmentFlow(Long id);

    /**
     * 获得治疗流程模板分页
     *
     * @param pageReqVO 分页查询
     * @return 治疗流程模板分页
     */
    PageResult<TreatmentFlowDO> getTreatmentFlowPage(PageParam pageReqVO);

    /**
     * 添加计划
     *
     * @param reqVO
     */
    Long addPlan(FlowPlanReqVO reqVO);

    /**
     * 更新计划
     *
     * @param reqVO
     */
    void updatePlan(FlowPlanReqVO reqVO);

    /**
     * 删除计划
     *
     * @param id
     */
    void delPlan(Long id);

    /**
     * 创建计划子任务
     *
     * @param vo
     * @return
     */
    Long createPlanTask(FlowTaskVO vo);

    /**
     * 更新计划子任务
     *
     * @param vo
     * @return
     */
    void updatePlanTask(FlowTaskVO vo);

    /**
     * 删除计划子任务
     *
     * @param id
     * @return
     */
    void delPlanTask(Long id);

    /**
     * 通过
     *
     * @param id
     * @return
     */
    List<TreatmentFlowDayDO> getPlanListByFlowId(Long id);

    /**
     * @param id
     * @return
     */
    List<TreatmentFlowDayitemDO> getTaskListByDayId(Long id);

    TreatmentFlowDayitemDO getTask(Long id);

    void publish(Long id, Integer state);

    void updateDayInstanceStatus(TreatmentDayInstanceDO dayInstanceDO, boolean isSameDay);

    void finishDayItemInstance(Long dayItemInstanceId);


    void cancelTreatmentInstance(Long flowInstanceId);

    //    void completeDayInstance(TreatmentDayInstanceDO dayInstanceDO);
    TreatmentNextVO getInsertedNextVO(Long userId, Long treatmentInstanceId);

    void addGuideLanguageStep(Long userId, Long treatmentInstanceId, String content);

}