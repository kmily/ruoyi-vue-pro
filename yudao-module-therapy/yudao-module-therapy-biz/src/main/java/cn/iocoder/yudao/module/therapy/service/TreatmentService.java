package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;

import javax.validation.Valid;


public interface TreatmentService {
    Long initTreatmentInstance(Long userId, String treatmentCode);

    Long getCurrentTreatmentInstance(Long userId, String treatmentCode);

    TreatmentStepItem getNext(TreatmentStepItem userCurrentStep);

    void completeDayitemInstance(Long userId, Long treatmentInstanceId, Long dayItemInstanceId);

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


}
