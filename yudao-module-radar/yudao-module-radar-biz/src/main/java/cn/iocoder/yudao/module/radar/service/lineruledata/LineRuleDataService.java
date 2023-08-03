package cn.iocoder.yudao.module.radar.service.lineruledata;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 绊线数据 Service 接口
 *
 * @author 芋道源码
 */
public interface LineRuleDataService {

    /**
     * 创建绊线数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLineRuleData(@Valid LineRuleDataCreateReqVO createReqVO);

    /**
     * 更新绊线数据
     *
     * @param updateReqVO 更新信息
     */
    void updateLineRuleData(@Valid LineRuleDataUpdateReqVO updateReqVO);

    /**
     * 删除绊线数据
     *
     * @param id 编号
     */
    void deleteLineRuleData(Long id);

    /**
     * 获得绊线数据
     *
     * @param id 编号
     * @return 绊线数据
     */
    LineRuleDataDO getLineRuleData(Long id);

    /**
     * 获得绊线数据列表
     *
     * @param ids 编号
     * @return 绊线数据列表
     */
    List<LineRuleDataDO> getLineRuleDataList(Collection<Long> ids);

    /**
     * 获得绊线数据分页
     *
     * @param pageReqVO 分页查询
     * @return 绊线数据分页
     */
    PageResult<LineRuleDataDO> getLineRuleDataPage(LineRuleDataPageReqVO pageReqVO);

    /**
     * 获得绊线数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 绊线数据列表
     */
    List<LineRuleDataDO> getLineRuleDataList(LineRuleDataExportReqVO exportReqVO);

}
