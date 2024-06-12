package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.SignState;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SignInReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentScheduleSaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentScheduleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
public class TreatmentScheduleServiceImpl implements TreatmentScheduleService {
    @Resource
    private TreatmentScheduleMapper treatmentScheduleMapper;
    @Override
    public Long createTreatmentSchedule(TreatmentScheduleSaveReqVO createReqVO) {
        // 插入
        TreatmentScheduleDO treatmentSchedule = BeanUtils.toBean(createReqVO, TreatmentScheduleDO.class);
        treatmentSchedule.setUserId(getLoginUserId());
        treatmentSchedule.setState(SignState.UNSIGNED.getType());
        treatmentScheduleMapper.insert(treatmentSchedule);
        // 返回
        return treatmentSchedule.getId();
    }

    @Override
    public void deleteTreatmentSchedule(Long id) {
        // 校验存在
        this.validateTreatmentScheduleExists(id);
        // 删除
        treatmentScheduleMapper.deleteById(id);
    }

    private TreatmentScheduleDO validateTreatmentScheduleExists(Long id) {
        TreatmentScheduleDO scheduleDO=treatmentScheduleMapper.selectById(id);
        if (scheduleDO == null) {
            throw exception(TREATMENT_SCHEDULE_NOT_EXISTS);
        }
        if(scheduleDO.getState().equals(SignState.SIGNED.getType())){
            throw exception(TREATMENT_SCHEDULE_SIGNED);
        }

        return scheduleDO;
    }

    @Override
    public TreatmentScheduleDO getTreatmentSchedule(Long id) {
        return treatmentScheduleMapper.selectById(id);
    }

    @Override
    public void signIn(SignInReqVO reqVO) {
        // 校验存在
        TreatmentScheduleDO sc=validateTreatmentScheduleExists(reqVO.getId());
        //校验当前时间是否在日程内
        if(!LocalDateTimeUtils.isBetween(sc.getBeginTime(),sc.getEndTime())){
            throw exception(TREATMENT_SCHEDULE_SIGNED_NOW_NOT_BETWEEN);
        }
        TreatmentScheduleDO scheduleDO= BeanUtils.toBean(reqVO, TreatmentScheduleDO.class);
        scheduleDO.setSignInTime(LocalDateTime.now());
        scheduleDO.setState(SignState.SIGNED.getType());
        treatmentScheduleMapper.updateById(scheduleDO);
    }

    @Override
    public List<TreatmentScheduleDO> getScheduleList(LocalDate day) {
        return treatmentScheduleMapper.getScheduleList(day);
    }
}
