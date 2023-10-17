package cn.iocoder.yudao.module.product.service.unit;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.product.controller.admin.unit.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.unit.UnitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 计量单位 Service 接口
 *
 * @author 芋道源码
 */
public interface UnitService {

    /**
     * 创建计量单位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUnit(@Valid UnitCreateReqVO createReqVO);

    /**
     * 更新计量单位
     *
     * @param updateReqVO 更新信息
     */
    void updateUnit(@Valid UnitUpdateReqVO updateReqVO);

    /**
     * 删除计量单位
     *
     * @param id 编号
     */
    void deleteUnit(Long id);

    /**
     * 获得计量单位
     *
     * @param id 编号
     * @return 计量单位
     */
    UnitDO getUnit(Long id);

    /**
     * 获得计量单位列表
     *
     * @param ids 编号
     * @return 计量单位列表
     */
    List<UnitDO> getUnitList(Collection<Long> ids);

    /**
     * 获得计量单位分页
     *
     * @param pageReqVO 分页查询
     * @return 计量单位分页
     */
    PageResult<UnitDO> getUnitPage(UnitPageReqVO pageReqVO);

    /**
     * 获得计量单位列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 计量单位列表
     */
    List<UnitDO> getUnitList(UnitExportReqVO exportReqVO);

}
