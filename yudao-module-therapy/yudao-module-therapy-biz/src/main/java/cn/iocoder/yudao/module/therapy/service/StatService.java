package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;

import java.util.List;

public interface StatService {
    /**
     * 行为活动计划统计
     * @param dayNum
     * @return
     */
    List<ScheduleStateRespVO> StatSchedule(Integer dayNum,Long userId);
}
