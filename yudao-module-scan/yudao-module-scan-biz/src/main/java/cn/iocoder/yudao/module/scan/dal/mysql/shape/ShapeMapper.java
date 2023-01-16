package cn.iocoder.yudao.module.scan.dal.mysql.shape;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;

/**
 * 体型 Mapper
 *
 * @author lyz
 */
@Mapper
public interface ShapeMapper extends BaseMapperX<ShapeDO> {

    default PageResult<ShapeDO> selectPage(ShapePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShapeDO>()
                .betweenIfPresent(ShapeDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(ShapeDO::getShapeName, reqVO.getShapeName())
                .eqIfPresent(ShapeDO::getShapeValue, reqVO.getShapeValue())
                .orderByDesc(ShapeDO::getId));
    }

    default List<ShapeDO> selectList(ShapeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ShapeDO>()
                .betweenIfPresent(ShapeDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(ShapeDO::getShapeName, reqVO.getShapeName())
                .eqIfPresent(ShapeDO::getShapeValue, reqVO.getShapeValue())
                .orderByDesc(ShapeDO::getId));
    }

}
