package com.cw.module.trade.convert.position;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cw.module.trade.controller.admin.position.vo.*;
import com.cw.module.trade.dal.dataobject.position.PositionDO;

/**
 * 账户持仓信息 Convert
 *
 * @author chengjiale
 */
@Mapper
public interface PositionConvert {

    PositionConvert INSTANCE = Mappers.getMapper(PositionConvert.class);

    PositionDO convert(PositionCreateReqVO bean);

    PositionDO convert(PositionUpdateReqVO bean);

    PositionRespVO convert(PositionDO bean);

    List<PositionRespVO> convertList(List<PositionDO> list);

    PageResult<PositionRespVO> convertPage(PageResult<PositionDO> page);

    List<PositionExcelVO> convertList02(List<PositionDO> list);

}
