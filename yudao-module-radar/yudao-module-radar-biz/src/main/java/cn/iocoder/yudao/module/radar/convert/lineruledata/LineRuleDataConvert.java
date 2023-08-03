package cn.iocoder.yudao.module.radar.convert.lineruledata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;

/**
 * 绊线数据 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface LineRuleDataConvert {

    LineRuleDataConvert INSTANCE = Mappers.getMapper(LineRuleDataConvert.class);

    LineRuleDataDO convert(LineRuleDataCreateReqVO bean);

    LineRuleDataDO convert(LineRuleDataUpdateReqVO bean);

    LineRuleDataRespVO convert(LineRuleDataDO bean);

    List<LineRuleDataRespVO> convertList(List<LineRuleDataDO> list);

    PageResult<LineRuleDataRespVO> convertPage(PageResult<LineRuleDataDO> page);

    List<LineRuleDataExcelVO> convertList02(List<LineRuleDataDO> list);

}
