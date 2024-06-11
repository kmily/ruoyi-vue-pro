package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SignInReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentScheduleSaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TreatmentScheduleService {
    /**
     * 创建患者日程
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTreatmentSchedule(@Valid TreatmentScheduleSaveReqVO createReqVO);


    /**
     * 删除患者日程
     *
     * @param id 编号
     */
    void deleteTreatmentSchedule(Long id);

    /**
     * 获得患者日程
     *
     * @param id 编号
     * @return 患者日程
     */
    TreatmentScheduleDO getTreatmentSchedule(Long id);

    void signIn(SignInReqVO reqVO);

    List<TreatmentScheduleDO> getScheduleList(LocalDate day);

//    /**
//     * 获得患者日程分页
//     *
//     * @param pageReqVO 分页查询
//     * @return 患者日程分页
//     */
//    PageResult<TreatmentScheduleDO> getTreatmentSchedulePage(TreatmentSchedulePageReqVO pageReqVO);

}
