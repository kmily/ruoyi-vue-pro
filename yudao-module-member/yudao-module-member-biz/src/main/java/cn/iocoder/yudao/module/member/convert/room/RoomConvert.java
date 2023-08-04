package cn.iocoder.yudao.module.member.convert.room;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.room.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;

/**
 * 用户房间 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomConvert {

    RoomConvert INSTANCE = Mappers.getMapper(RoomConvert.class);

    RoomDO convert(RoomCreateReqVO bean);

    RoomDO convert(RoomUpdateReqVO bean);

    RoomRespVO convert(RoomDO bean);

    List<RoomRespVO> convertList(List<RoomDO> list);

    PageResult<RoomRespVO> convertPage(PageResult<RoomDO> page);

    List<RoomExcelVO> convertList02(List<RoomDO> list);

}
