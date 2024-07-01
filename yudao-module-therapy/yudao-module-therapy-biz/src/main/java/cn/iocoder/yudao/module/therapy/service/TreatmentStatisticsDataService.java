package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.TreatmentPlanVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import scala.Int;

import java.util.List;
import java.util.Map;

public interface TreatmentStatisticsDataService {

    /**
     * 查询心理困扰分类
     *
     * @param treatmentInstanceIds
     * @return
     * e.g.
     * param: [1, 2, 3]
     * return : {1=[A, B, C], 2=[B, C], 3=[]}
     */
    Map<Long, List<String>> queryPsycoTroubleCategory(List<Long> treatmentInstanceIds);


    /**
     * 查询治疗进度
     *
     * @param treatmentInstanceId
     * @return
     */
    TreatmentProgressRespVO getTreatmentProgress(Long treatmentInstanceId);

    TreatmentPlanVO getTreatmentProgressAndPlan(Long treatmentInstanceId);



    Map<Long,TreatmentInstanceDO> queryLatestTreatmentInstanceId(List<Long> userIds);

    List<String> queryUserGoals(Long userId);

    /**
     * 查询用户的治疗进度百分比
     *
     * @param treatmentInstanceId
     * @return
     */
    int getProgressPercentage(Long treatmentInstanceId);


    /**
     * 获取治疗中\治疗完成、完成初步评估的人数统计数据
     */

    Map<String, Integer> getTreatmentUserCount(String startDate);

    List<String>  queryUserTroubles(Long userId);

    }
