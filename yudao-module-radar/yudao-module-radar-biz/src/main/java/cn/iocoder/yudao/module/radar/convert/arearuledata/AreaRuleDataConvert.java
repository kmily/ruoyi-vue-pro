package cn.iocoder.yudao.module.radar.convert.arearuledata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.bean.entity.AreaRuleData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata.AreaRuleDataDO;

/**
 * 区域数据 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface AreaRuleDataConvert {

    AreaRuleDataConvert INSTANCE = Mappers.getMapper(AreaRuleDataConvert.class);

    AreaRuleDataDO convert(AreaRuleDataCreateReqVO bean);

    AreaRuleDataDO convert(AreaRuleDataUpdateReqVO bean);

    AreaRuleDataRespVO convert(AreaRuleDataDO bean);

    List<AreaRuleDataRespVO> convertList(List<AreaRuleDataDO> list);

    PageResult<AreaRuleDataRespVO> convertPage(PageResult<AreaRuleDataDO> page);

    List<AreaRuleDataExcelVO> convertList02(List<AreaRuleDataDO> list);

    AreaRuleDataCreateReqVO convert(AreaRuleData areaRuleData);
}
