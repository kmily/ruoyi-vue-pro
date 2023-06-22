package com.cw.module.trade.convert.notifymsg;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cw.module.trade.controller.admin.notifymsg.vo.*;
import com.cw.module.trade.dal.dataobject.notifymsg.NotifyMsgDO;

/**
 * 账号通知记录 Convert
 *
 * @author chengjiale
 */
@Mapper
public interface NotifyMsgConvert {

    NotifyMsgConvert INSTANCE = Mappers.getMapper(NotifyMsgConvert.class);

    NotifyMsgDO convert(NotifyMsgCreateReqVO bean);

    NotifyMsgDO convert(NotifyMsgUpdateReqVO bean);

    NotifyMsgRespVO convert(NotifyMsgDO bean);

    List<NotifyMsgRespVO> convertList(List<NotifyMsgDO> list);

    PageResult<NotifyMsgRespVO> convertPage(PageResult<NotifyMsgDO> page);

    List<NotifyMsgExcelVO> convertList02(List<NotifyMsgDO> list);

}
