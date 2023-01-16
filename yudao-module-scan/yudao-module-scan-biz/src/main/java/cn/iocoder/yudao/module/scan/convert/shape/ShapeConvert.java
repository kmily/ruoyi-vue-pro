package cn.iocoder.yudao.module.scan.convert.shape;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;
import cn.iocoder.yudao.module.scan.controller.app.shape.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;

/**
 * 体型 Convert
 *
 * @author lyz
 */
@Mapper
public interface ShapeConvert {

    ShapeConvert INSTANCE = Mappers.getMapper(ShapeConvert.class);

    ShapeDO convert(ShapeCreateReqVO bean);

    ShapeDO convert(ShapeUpdateReqVO bean);

    ShapeRespVO convert(ShapeDO bean);

    List<ShapeRespVO> convertList(List<ShapeDO> list);


    PageResult<ShapeRespVO> convertPage(PageResult<ShapeDO> page);

    List<ShapeExcelVO> convertList02(List<ShapeDO> list);
    List<AppShapeRespVO> convertList03(List<ShapeDO> list);
    AppShapeRespVO convert02(ShapeDO bean);

}
