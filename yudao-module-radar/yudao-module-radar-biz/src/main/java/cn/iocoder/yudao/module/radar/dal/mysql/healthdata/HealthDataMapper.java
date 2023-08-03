package cn.iocoder.yudao.module.radar.dal.mysql.healthdata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;

/**
 * 体征数据 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthDataMapper extends BaseMapperX<HealthDataDO> {

    default PageResult<HealthDataDO> selectPage(HealthDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HealthDataDO>()
                .eqIfPresent(HealthDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(HealthDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(HealthDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(HealthDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(HealthDataDO::getHasPeople, reqVO.getHasPeople())
                .eqIfPresent(HealthDataDO::getHasMove, reqVO.getHasMove())
                .eqIfPresent(HealthDataDO::getBreathFreqAverage, reqVO.getBreathFreqAverage())
                .eqIfPresent(HealthDataDO::getHeartFreqAverage, reqVO.getHeartFreqAverage())
                .eqIfPresent(HealthDataDO::getBreathAmpAverage, reqVO.getBreathAmpAverage())
                .eqIfPresent(HealthDataDO::getHeartAmpAverage, reqVO.getHeartAmpAverage())
                .eqIfPresent(HealthDataDO::getBreathFreqStd, reqVO.getBreathFreqStd())
                .eqIfPresent(HealthDataDO::getHeartFreqStd, reqVO.getHeartFreqStd())
                .eqIfPresent(HealthDataDO::getBreathAmpDiff, reqVO.getBreathAmpDiff())
                .eqIfPresent(HealthDataDO::getHeartAmpDiff, reqVO.getHeartAmpDiff())
                .eqIfPresent(HealthDataDO::getMeanibi, reqVO.getMeanibi())
                .eqIfPresent(HealthDataDO::getRmssd, reqVO.getRmssd())
                .eqIfPresent(HealthDataDO::getSdrr, reqVO.getSdrr())
                .eqIfPresent(HealthDataDO::getPnn50, reqVO.getPnn50())
                .betweenIfPresent(HealthDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthDataDO::getId));
    }

    default List<HealthDataDO> selectList(HealthDataExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<HealthDataDO>()
                .eqIfPresent(HealthDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(HealthDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(HealthDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(HealthDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(HealthDataDO::getHasPeople, reqVO.getHasPeople())
                .eqIfPresent(HealthDataDO::getHasMove, reqVO.getHasMove())
                .eqIfPresent(HealthDataDO::getBreathFreqAverage, reqVO.getBreathFreqAverage())
                .eqIfPresent(HealthDataDO::getHeartFreqAverage, reqVO.getHeartFreqAverage())
                .eqIfPresent(HealthDataDO::getBreathAmpAverage, reqVO.getBreathAmpAverage())
                .eqIfPresent(HealthDataDO::getHeartAmpAverage, reqVO.getHeartAmpAverage())
                .eqIfPresent(HealthDataDO::getBreathFreqStd, reqVO.getBreathFreqStd())
                .eqIfPresent(HealthDataDO::getHeartFreqStd, reqVO.getHeartFreqStd())
                .eqIfPresent(HealthDataDO::getBreathAmpDiff, reqVO.getBreathAmpDiff())
                .eqIfPresent(HealthDataDO::getHeartAmpDiff, reqVO.getHeartAmpDiff())
                .eqIfPresent(HealthDataDO::getMeanibi, reqVO.getMeanibi())
                .eqIfPresent(HealthDataDO::getRmssd, reqVO.getRmssd())
                .eqIfPresent(HealthDataDO::getSdrr, reqVO.getSdrr())
                .eqIfPresent(HealthDataDO::getPnn50, reqVO.getPnn50())
                .betweenIfPresent(HealthDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthDataDO::getId));
    }

}
