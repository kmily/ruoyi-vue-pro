package com.cw.module.trade.convert.followrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.cw.module.trade.controller.admin.followrecord.vo.*;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;

/**
 * 账号跟随记录 Convert
 *
 * @author chengjiale
 */
@Mapper
public interface FollowRecordConvert {

    FollowRecordConvert INSTANCE = Mappers.getMapper(FollowRecordConvert.class);

    FollowRecordDO convert(FollowRecordCreateReqVO bean);

    FollowRecordDO convert(FollowRecordUpdateReqVO bean);

    FollowRecordRespVO convert(FollowRecordDO bean);

    List<FollowRecordRespVO> convertList(List<FollowRecordDO> list);

    PageResult<FollowRecordRespVO> convertPage(PageResult<FollowRecordDO> page);

    List<FollowRecordExcelVO> convertList02(List<FollowRecordDO> list);

}
