package cn.iocoder.yudao.module.radar.convert.device;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.device.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.device.DeviceDO;

/**
 * 设备信息 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceConvert {

    DeviceConvert INSTANCE = Mappers.getMapper(DeviceConvert.class);

    DeviceDO convert(DeviceCreateReqVO bean);

    DeviceDO convert(DeviceUpdateReqVO bean);

    DeviceRespVO convert(DeviceDO bean);

    List<DeviceRespVO> convertList(List<DeviceDO> list);

    PageResult<DeviceRespVO> convertPage(PageResult<DeviceDO> page);

    List<DeviceExcelVO> convertList02(List<DeviceDO> list);

    DeviceDTO convertDTO(DeviceDO bean);

}
