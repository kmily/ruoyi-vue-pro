package cn.iocoder.yudao.module.member.convert.bootup;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.bootup.BootUpDO;

/**
 * 用户启动数据 Convert
 *
 * @author 和尘同光
 */
@Mapper
public interface BootUpConvert {

    BootUpConvert INSTANCE = Mappers.getMapper(BootUpConvert.class);

    BootUpDO convert(BootUpCreateReqVO bean);

    BootUpDO convert(BootUpUpdateReqVO bean);

    BootUpRespVO convert(BootUpDO bean);

    List<BootUpRespVO> convertList(List<BootUpDO> list);

    PageResult<BootUpRespVO> convertPage(PageResult<BootUpDO> page);

    List<BootUpExcelVO> convertList02(List<BootUpDO> list);
    List<BootUpExcelVO> convertList03(List<BootUpListResVO> list);

}
