package cn.iocoder.yudao.module.scan.dal.mysql.shapesolution;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;

/**
 * 体型解决方案 Mapper
 *
 * @author lyz
 */
@Mapper
public interface ShapeSolutionMapper extends BaseMapperX<ShapeSolutionDO> {

    default PageResult<ShapeSolutionDO> selectPage(ShapeSolutionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShapeSolutionDO>()
                .betweenIfPresent(ShapeSolutionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShapeSolutionDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ShapeSolutionDO::getType, reqVO.getType())
                .eqIfPresent(ShapeSolutionDO::getContent, reqVO.getContent())
                .eqIfPresent(ShapeSolutionDO::getShapeShapeId, reqVO.getShapeShapeId())
                .eqIfPresent(ShapeSolutionDO::getSort, reqVO.getSort())
                .orderByDesc(ShapeSolutionDO::getId));
    }

    default List<ShapeSolutionDO> selectList(ShapeSolutionExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ShapeSolutionDO>()
                .betweenIfPresent(ShapeSolutionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShapeSolutionDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ShapeSolutionDO::getType, reqVO.getType())
                .eqIfPresent(ShapeSolutionDO::getContent, reqVO.getContent())
                .eqIfPresent(ShapeSolutionDO::getShapeShapeId, reqVO.getShapeShapeId())
                .eqIfPresent(ShapeSolutionDO::getSort, reqVO.getSort())
                .orderByDesc(ShapeSolutionDO::getId));
    }

}
