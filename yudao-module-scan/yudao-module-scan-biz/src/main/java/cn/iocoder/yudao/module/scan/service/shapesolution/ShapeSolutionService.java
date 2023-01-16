package cn.iocoder.yudao.module.scan.service.shapesolution;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 体型解决方案 Service 接口
 *
 * @author lyz
 */
public interface ShapeSolutionService {

    /**
     * 创建体型解决方案
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShapeSolution(@Valid ShapeSolutionCreateReqVO createReqVO);

    /**
     * 更新体型解决方案
     *
     * @param updateReqVO 更新信息
     */
    void updateShapeSolution(@Valid ShapeSolutionUpdateReqVO updateReqVO);

    /**
     * 删除体型解决方案
     *
     * @param id 编号
     */
    void deleteShapeSolution(Long id);

    /**
     * 获得体型解决方案
     *
     * @param id 编号
     * @return 体型解决方案
     */
    ShapeSolutionDO getShapeSolution(Long id);

    /**
     * 获得体型解决方案列表
     *
     * @param ids 编号
     * @return 体型解决方案列表
     */
    List<ShapeSolutionDO> getShapeSolutionList(Collection<Long> ids);

    /**
     * 获得体型解决方案分页
     *
     * @param pageReqVO 分页查询
     * @return 体型解决方案分页
     */
    PageResult<ShapeSolutionDO> getShapeSolutionPage(ShapeSolutionPageReqVO pageReqVO);

    /**
     * 获得体型解决方案列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 体型解决方案列表
     */
    List<ShapeSolutionDO> getShapeSolutionList(ShapeSolutionExportReqVO exportReqVO);

}
