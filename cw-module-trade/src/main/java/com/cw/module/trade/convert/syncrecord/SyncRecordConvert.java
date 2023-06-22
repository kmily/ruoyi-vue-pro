package com.cw.module.trade.convert.syncrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cw.module.trade.controller.admin.syncrecord.vo.*;
import com.cw.module.trade.dal.dataobject.syncrecord.SyncRecordDO;

/**
 * 账号同步记录 Convert
 *
 * @author chengjiale
 */
@Mapper
public interface SyncRecordConvert {

    SyncRecordConvert INSTANCE = Mappers.getMapper(SyncRecordConvert.class);

    SyncRecordDO convert(SyncRecordCreateReqVO bean);

    SyncRecordDO convert(SyncRecordUpdateReqVO bean);

    SyncRecordRespVO convert(SyncRecordDO bean);

    List<SyncRecordRespVO> convertList(List<SyncRecordDO> list);

    PageResult<SyncRecordRespVO> convertPage(PageResult<SyncRecordDO> page);

    List<SyncRecordExcelVO> convertList02(List<SyncRecordDO> list);

}
