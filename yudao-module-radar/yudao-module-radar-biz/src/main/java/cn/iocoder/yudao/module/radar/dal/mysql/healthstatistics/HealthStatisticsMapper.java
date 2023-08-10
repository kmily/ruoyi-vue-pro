package cn.iocoder.yudao.module.radar.dal.mysql.healthstatistics;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics.HealthStatisticsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.*;

/**
 * 睡眠统计记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthStatisticsMapper extends BaseMapperX<HealthStatisticsDO> {

    default PageResult<HealthStatisticsDO> selectPage(HealthStatisticsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HealthStatisticsDO>()
                .eqIfPresent(HealthStatisticsDO::getDeviceId, reqVO.getDeviceId())
                .betweenIfPresent(HealthStatisticsDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(HealthStatisticsDO::getBreathMaxValue, reqVO.getBreathMaxValue())
                .eqIfPresent(HealthStatisticsDO::getBreathMinValue, reqVO.getBreathMinValue())
                .eqIfPresent(HealthStatisticsDO::getBreathSilent, reqVO.getBreathSilent())
                .eqIfPresent(HealthStatisticsDO::getBreathAverage, reqVO.getBreathAverage())
                .eqIfPresent(HealthStatisticsDO::getHeartMaxValue, reqVO.getHeartMaxValue())
                .eqIfPresent(HealthStatisticsDO::getHeartMinValue, reqVO.getHeartMinValue())
                .eqIfPresent(HealthStatisticsDO::getHeartSilent, reqVO.getHeartSilent())
                .eqIfPresent(HealthStatisticsDO::getHeartAverage, reqVO.getHeartAverage())
                .betweenIfPresent(HealthStatisticsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthStatisticsDO::getId));
    }

    default List<HealthStatisticsDO> selectList(HealthStatisticsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<HealthStatisticsDO>()
                .eqIfPresent(HealthStatisticsDO::getDeviceId, reqVO.getDeviceId())
                .betweenIfPresent(HealthStatisticsDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(HealthStatisticsDO::getBreathMaxValue, reqVO.getBreathMaxValue())
                .eqIfPresent(HealthStatisticsDO::getBreathMinValue, reqVO.getBreathMinValue())
                .eqIfPresent(HealthStatisticsDO::getBreathSilent, reqVO.getBreathSilent())
                .eqIfPresent(HealthStatisticsDO::getBreathAverage, reqVO.getBreathAverage())
                .eqIfPresent(HealthStatisticsDO::getHeartMaxValue, reqVO.getHeartMaxValue())
                .eqIfPresent(HealthStatisticsDO::getHeartMinValue, reqVO.getHeartMinValue())
                .eqIfPresent(HealthStatisticsDO::getHeartSilent, reqVO.getHeartSilent())
                .eqIfPresent(HealthStatisticsDO::getHeartAverage, reqVO.getHeartAverage())
                .betweenIfPresent(HealthStatisticsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthStatisticsDO::getId));
    }

}
