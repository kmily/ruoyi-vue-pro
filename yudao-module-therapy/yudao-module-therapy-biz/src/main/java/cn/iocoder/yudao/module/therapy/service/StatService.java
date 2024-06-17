package cn.iocoder.yudao.module.therapy.service;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatService {
    /**
     * 行为活动计划统计
     * @param dayNum
     * @return
     */
    List<ScheduleStateRespVO> StatSchedule(Integer dayNum,Long userId);

    /**
     * 获取量表报告
     * @param answerId
     * @param userId
     * @return
     */
    Map<String,String> getScaleReport(Long answerId,Long userId);

    /**
     * 获取量表列表
     * @param userId
     * @return
     */
    List<SurveyAnswerDO> getScaleList(Long userId);

    /**
     * 获取量表列表
     * @param userId
     * @return
     */
    Map<String, List<KeyValue>> getScaleChart(Long userId);

    /**
     * 获取心情评分
     * @param userId
     * @return
     */
    List<SurveyAnswerDO> getAnswerList(Long userId, LocalDate begin, LocalDate end,List<Integer> types);

    /**
     * 获取对策卡
     * @param userId
     * @return
     */
    Map<String,List<JSONObject>> getStrategyCard(Long userId);

}
