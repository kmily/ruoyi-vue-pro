package cn.iocoder.yudao.module.radar.service.healthstatistics;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics.HealthStatisticsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.healthstatistics.HealthStatisticsConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.healthstatistics.HealthStatisticsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 睡眠统计记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HealthStatisticsServiceImpl implements HealthStatisticsService {

    @Resource
    private HealthStatisticsMapper healthStatisticsMapper;

    @Override
    public Long createHealthStatistics(HealthStatisticsCreateReqVO createReqVO) {
        // 插入
        HealthStatisticsDO healthStatistics = HealthStatisticsConvert.INSTANCE.convert(createReqVO);

        HealthStatisticsDO statisticsDO = healthStatisticsMapper.selectOne(HealthStatisticsDO::getDeviceId, createReqVO.getDeviceId(),
                HealthStatisticsDO::getStartTime, createReqVO.getStartTime());
        if(statisticsDO == null){
            healthStatisticsMapper.insert(healthStatistics);
        }else{
            healthStatisticsMapper.updateById(healthStatistics.setId(statisticsDO.getId()));
        }
        // 返回
        return healthStatistics.getId();
    }

    @Override
    public void updateHealthStatistics(HealthStatisticsUpdateReqVO updateReqVO) {
        // 校验存在
        validateHealthStatisticsExists(updateReqVO.getId());
        // 更新
        HealthStatisticsDO updateObj = HealthStatisticsConvert.INSTANCE.convert(updateReqVO);
        healthStatisticsMapper.updateById(updateObj);
    }

    @Override
    public void deleteHealthStatistics(Long id) {
        // 校验存在
        validateHealthStatisticsExists(id);
        // 删除
        healthStatisticsMapper.deleteById(id);
    }

    private void validateHealthStatisticsExists(Long id) {
        if (healthStatisticsMapper.selectById(id) == null) {
            throw exception(HEALTH_STATISTICS_NOT_EXISTS);
        }
    }

    @Override
    public HealthStatisticsDO getHealthStatistics(Long id) {
        return healthStatisticsMapper.selectById(id);
    }

    @Override
    public List<HealthStatisticsDO> getHealthStatisticsList(Collection<Long> ids) {
        return healthStatisticsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HealthStatisticsDO> getHealthStatisticsPage(HealthStatisticsPageReqVO pageReqVO) {
        return healthStatisticsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HealthStatisticsDO> getHealthStatisticsList(HealthStatisticsExportReqVO exportReqVO) {
        return healthStatisticsMapper.selectList(exportReqVO);
    }

    @Override
    public void createHealthStatistics(List<HealthStatisticsCreateReqVO> createReqVOS) {
        //List<HealthStatisticsDO> list = HealthStatisticsConvert.INSTANCE.convertList01(createReqVOS);
      //  healthStatisticsMapper.insertBatch(list, 100);
    }

    @Override
    public List<HealthStatisticsDO> getHealthStatisticsList(Long deviceId, String startDate, String endDate) {
        return healthStatisticsMapper.selectList(new LambdaQueryWrapperX<HealthStatisticsDO>()
                .eqIfPresent(HealthStatisticsDO::getDeviceId, deviceId)
                .between(HealthStatisticsDO::getStartTime, startDate, endDate)
                .orderByAsc(HealthStatisticsDO::getStartTime));
    }

}
