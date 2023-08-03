package cn.iocoder.yudao.module.radar.service.healthdata;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 体征数据 Service 接口
 *
 * @author 芋道源码
 */
public interface HealthDataService {

    /**
     * 创建体征数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHealthData(@Valid HealthDataCreateReqVO createReqVO);

    /**
     * 更新体征数据
     *
     * @param updateReqVO 更新信息
     */
    void updateHealthData(@Valid HealthDataUpdateReqVO updateReqVO);

    /**
     * 删除体征数据
     *
     * @param id 编号
     */
    void deleteHealthData(Long id);

    /**
     * 获得体征数据
     *
     * @param id 编号
     * @return 体征数据
     */
    HealthDataDO getHealthData(Long id);

    /**
     * 获得体征数据列表
     *
     * @param ids 编号
     * @return 体征数据列表
     */
    List<HealthDataDO> getHealthDataList(Collection<Long> ids);

    /**
     * 获得体征数据分页
     *
     * @param pageReqVO 分页查询
     * @return 体征数据分页
     */
    PageResult<HealthDataDO> getHealthDataPage(HealthDataPageReqVO pageReqVO);

    /**
     * 获得体征数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 体征数据列表
     */
    List<HealthDataDO> getHealthDataList(HealthDataExportReqVO exportReqVO);

}
