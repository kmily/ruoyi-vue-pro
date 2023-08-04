package cn.iocoder.yudao.module.member.convert.deviceuser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;

/**
 * 设备和用户绑定 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUserConvert {

    DeviceUserConvert INSTANCE = Mappers.getMapper(DeviceUserConvert.class);

    DeviceUserDO convert(AppDeviceUserCreateReqVO bean);

    DeviceUserDO convert(AppDeviceUserUpdateReqVO bean);

    AppDeviceUserRespVO convert(DeviceUserDO bean);

    List<AppDeviceUserRespVO> convertList(List<DeviceUserDO> list);

    PageResult<AppDeviceUserRespVO> convertPage(PageResult<DeviceUserDO> page);

    List<AppDeviceUserExcelVO> convertList02(List<DeviceUserDO> list);

    AppDeviceRespVO convert(DeviceDTO dto);

}
