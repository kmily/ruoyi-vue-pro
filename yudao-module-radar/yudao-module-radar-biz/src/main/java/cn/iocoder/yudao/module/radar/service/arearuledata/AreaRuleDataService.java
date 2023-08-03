package cn.iocoder.yudao.module.radar.service.arearuledata;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata.AreaRuleDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 区域数据 Service 接口
 *
 * @author 芋道源码
 */
public interface AreaRuleDataService {

    /**
     * 创建区域数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAreaRuleData(@Valid AreaRuleDataCreateReqVO createReqVO);

    /**
     * 更新区域数据
     *
     * @param updateReqVO 更新信息
     */
    void updateAreaRuleData(@Valid AreaRuleDataUpdateReqVO updateReqVO);

    /**
     * 删除区域数据
     *
     * @param id 编号
     */
    void deleteAreaRuleData(Long id);

    /**
     * 获得区域数据
     *
     * @param id 编号
     * @return 区域数据
     */
    AreaRuleDataDO getAreaRuleData(Long id);

    /**
     * 获得区域数据列表
     *
     * @param ids 编号
     * @return 区域数据列表
     */
    List<AreaRuleDataDO> getAreaRuleDataList(Collection<Long> ids);

    /**
     * 获得区域数据分页
     *
     * @param pageReqVO 分页查询
     * @return 区域数据分页
     */
    PageResult<AreaRuleDataDO> getAreaRuleDataPage(AreaRuleDataPageReqVO pageReqVO);

    /**
     * 获得区域数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 区域数据列表
     */
    List<AreaRuleDataDO> getAreaRuleDataList(AreaRuleDataExportReqVO exportReqVO);

}
