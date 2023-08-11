package cn.iocoder.yudao.module.member.convert.devicenotice;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;

/**
 * 设备通知 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceNoticeConvert {

    DeviceNoticeConvert INSTANCE = Mappers.getMapper(DeviceNoticeConvert.class);

    DeviceNoticeDO convert(AppDeviceNoticeCreateReqVO bean);

    DeviceNoticeDO convert(AppDeviceNoticeUpdateReqVO bean);

    AppDeviceNoticeRespVO convert(DeviceNoticeDO bean);

    List<AppDeviceNoticeRespVO> convertList(List<DeviceNoticeDO> list);

    PageResult<AppDeviceNoticeRespVO> convertPage(PageResult<DeviceNoticeDO> page);

    List<AppDeviceNoticeExcelVO> convertList02(List<DeviceNoticeDO> list);

}
