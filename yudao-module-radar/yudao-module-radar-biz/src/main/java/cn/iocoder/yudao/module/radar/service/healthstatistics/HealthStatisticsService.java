package cn.iocoder.yudao.module.radar.service.healthstatistics;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics.HealthStatisticsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 睡眠统计记录 Service 接口
 *
 * @author 芋道源码
 */
public interface HealthStatisticsService {

    /**
     * 创建睡眠统计记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHealthStatistics(@Valid HealthStatisticsCreateReqVO createReqVO);

    /**
     * 更新睡眠统计记录
     *
     * @param updateReqVO 更新信息
     */
    void updateHealthStatistics(@Valid HealthStatisticsUpdateReqVO updateReqVO);

    /**
     * 删除睡眠统计记录
     *
     * @param id 编号
     */
    void deleteHealthStatistics(Long id);

    /**
     * 获得睡眠统计记录
     *
     * @param id 编号
     * @return 睡眠统计记录
     */
    HealthStatisticsDO getHealthStatistics(Long id);

    /**
     * 获得睡眠统计记录列表
     *
     * @param ids 编号
     * @return 睡眠统计记录列表
     */
    List<HealthStatisticsDO> getHealthStatisticsList(Collection<Long> ids);

    /**
     * 获得睡眠统计记录分页
     *
     * @param pageReqVO 分页查询
     * @return 睡眠统计记录分页
     */
    PageResult<HealthStatisticsDO> getHealthStatisticsPage(HealthStatisticsPageReqVO pageReqVO);

    /**
     * 获得睡眠统计记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 睡眠统计记录列表
     */
    List<HealthStatisticsDO> getHealthStatisticsList(HealthStatisticsExportReqVO exportReqVO);

    void createHealthStatistics(List<HealthStatisticsCreateReqVO> createReqVOS);

    /**
     * 根据日期查询统计信息
     * @param deviceId
     * @param startDate
     * @param endDate
     * @return
     */
    List<HealthStatisticsDO> getHealthStatisticsList(Long deviceId, String startDate, String endDate);
}
