package cn.iocoder.yudao.module.member.convert.serverperson;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;

/**
 * 被服务人 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerPersonConvert {

    ServerPersonConvert INSTANCE = Mappers.getMapper(ServerPersonConvert.class);

    ServerPersonDO convert(AppServerPersonCreateReqVO bean);

    ServerPersonDO convert(AppServerPersonUpdateReqVO bean);

    AppServerPersonRespVO convert(ServerPersonDO bean);

    ServerPersonRespVO convert01(ServerPersonDO bean);

    List<AppServerPersonRespVO> convertList(List<ServerPersonDO> list);

    PageResult<AppServerPersonRespVO> convertPage(PageResult<ServerPersonDO> page);
    PageResult<ServerPersonRespVO> convertPage01(PageResult<ServerPersonDO> page);

    AppServerPersonPageReqVO convert(ServerPersonPageReqVO pageVO);
}
