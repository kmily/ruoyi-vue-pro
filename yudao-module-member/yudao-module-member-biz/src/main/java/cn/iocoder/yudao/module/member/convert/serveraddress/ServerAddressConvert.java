package cn.iocoder.yudao.module.member.convert.serveraddress;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;

/**
 * 服务地址 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerAddressConvert {

    ServerAddressConvert INSTANCE = Mappers.getMapper(ServerAddressConvert.class);

    ServerAddressDO convert(ServerAddressCreateReqVO bean);

    ServerAddressDO convert(ServerAddressUpdateReqVO bean);

    ServerAddressRespVO convert(ServerAddressDO bean);

    List<ServerAddressRespVO> convertList(List<ServerAddressDO> list);

    PageResult<ServerAddressRespVO> convertPage(PageResult<ServerAddressDO> page);

    List<ServerAddressExcelVO> convertList02(List<ServerAddressDO> list);

}
