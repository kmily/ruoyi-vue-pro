package cn.iocoder.yudao.module.radar.convert.deviceuser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.deviceuser.DeviceUserDO;

/**
 * 设备和用户绑定 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUserConvert {

    DeviceUserConvert INSTANCE = Mappers.getMapper(DeviceUserConvert.class);

    DeviceUserDO convert(DeviceUserCreateReqVO bean);

    DeviceUserDO convert(DeviceUserUpdateReqVO bean);

    DeviceUserRespVO convert(DeviceUserDO bean);

    List<DeviceUserRespVO> convertList(List<DeviceUserDO> list);

    PageResult<DeviceUserRespVO> convertPage(PageResult<DeviceUserDO> page);

    List<DeviceUserExcelVO> convertList02(List<DeviceUserDO> list);

}
