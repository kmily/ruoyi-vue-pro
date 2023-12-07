package cn.iocoder.yudao.module.member.convert.serverperson;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonCheckLogDO;

/**
 * 被户人审核记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerPersonCheckLogConvert {

    ServerPersonCheckLogConvert INSTANCE = Mappers.getMapper(ServerPersonCheckLogConvert.class);


    ServerPersonCheckLogRespVO convert(ServerPersonCheckLogDO bean);

    List<ServerPersonCheckLogRespVO> convertList(List<ServerPersonCheckLogDO> list);

    PageResult<ServerPersonCheckLogRespVO> convertPage(PageResult<ServerPersonCheckLogDO> page);

    ServerPersonCheckLogDO convert(ServerPersonAuditVO auditVO);

   default ServerPersonCheckLogDO convert(ServerPersonAuditVO auditVO, Long personId, String checkName){
       return convert(auditVO).setId(null).setCheckName(checkName).setPersonId(personId);
   }

}
