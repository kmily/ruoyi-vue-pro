package cn.iocoder.yudao.module.scan.convert.shapesolution;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.controller.app.shapesolution.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;

/**
 * 体型解决方案 Convert
 *
 * @author lyz
 */
@Mapper
public interface ShapeSolutionConvert {

    ShapeSolutionConvert INSTANCE = Mappers.getMapper(ShapeSolutionConvert.class);

    ShapeSolutionDO convert(ShapeSolutionCreateReqVO bean);

    ShapeSolutionDO convert(ShapeSolutionUpdateReqVO bean);

    ShapeSolutionRespVO convert(ShapeSolutionDO bean);

    List<ShapeSolutionRespVO> convertList(List<ShapeSolutionDO> list);

    PageResult<ShapeSolutionRespVO> convertPage(PageResult<ShapeSolutionDO> page);

    List<ShapeSolutionExcelVO> convertList02(List<ShapeSolutionDO> list);
    List<AppShapeSolutionRespVO> convertList03(List<ShapeSolutionDO> list);
    AppShapeSolutionRespVO  convert02(ShapeSolutionDO bean);
}
