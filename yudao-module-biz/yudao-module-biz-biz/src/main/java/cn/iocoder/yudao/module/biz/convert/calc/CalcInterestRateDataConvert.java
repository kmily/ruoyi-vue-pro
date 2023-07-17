package cn.iocoder.yudao.module.biz.convert.calc;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;

/**
 * 利率数据 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CalcInterestRateDataConvert {

    CalcInterestRateDataConvert INSTANCE = Mappers.getMapper(CalcInterestRateDataConvert.class);

    CalcInterestRateDataDO convert(CalcInterestRateDataCreateReqVO bean);

    CalcInterestRateDataDO convert(CalcInterestRateDataUpdateReqVO bean);

    CalcInterestRateDataRespVO convert(CalcInterestRateDataDO bean);

    List<CalcInterestRateDataRespVO> convertList(List<CalcInterestRateDataDO> list);

    PageResult<CalcInterestRateDataRespVO> convertPage(PageResult<CalcInterestRateDataDO> page);

    List<CalcInterestRateDataExcelVO> convertList02(List<CalcInterestRateDataDO> list);

}
