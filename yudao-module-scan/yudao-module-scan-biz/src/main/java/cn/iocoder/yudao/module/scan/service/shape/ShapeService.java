package cn.iocoder.yudao.module.scan.service.shape;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 体型 Service 接口
 *
 * @author lyz
 */
public interface ShapeService {

    /**
     * 创建体型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShape(@Valid ShapeCreateReqVO createReqVO);

    /**
     * 更新体型
     *
     * @param updateReqVO 更新信息
     */
    void updateShape(@Valid ShapeUpdateReqVO updateReqVO);

    /**
     * 删除体型
     *
     * @param id 编号
     */
    void deleteShape(Long id);

    /**
     * 获得体型
     *
     * @param id 编号
     * @return 体型
     */
    ShapeDO getShape(Long id);

    /**
     * 获得体型列表
     *
     * @param ids 编号
     * @return 体型列表
     */
    List<ShapeDO> getShapeList(Collection<Long> ids);

    /**
     * 获得体型分页
     *
     * @param pageReqVO 分页查询
     * @return 体型分页
     */
    PageResult<ShapeDO> getShapePage(ShapePageReqVO pageReqVO);

    /**
     * 获得体型列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 体型列表
     */
    List<ShapeDO> getShapeList(ShapeExportReqVO exportReqVO);

}
