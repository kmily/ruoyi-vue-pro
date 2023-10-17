package cn.iocoder.yudao.module.product.convert.unit;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.product.controller.admin.unit.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.unit.UnitDO;

/**
 * 计量单位 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface UnitConvert {

    UnitConvert INSTANCE = Mappers.getMapper(UnitConvert.class);

    UnitDO convert(UnitCreateReqVO bean);

    UnitDO convert(UnitUpdateReqVO bean);

    UnitRespVO convert(UnitDO bean);

    List<UnitRespVO> convertList(List<UnitDO> list);

    PageResult<UnitRespVO> convertPage(PageResult<UnitDO> page);

    List<UnitExcelVO> convertList02(List<UnitDO> list);

}
