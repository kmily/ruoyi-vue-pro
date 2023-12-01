package cn.iocoder.yudao.module.member.api.serverperson;

import cn.hutool.core.util.IdcardUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.member.api.serverperson.dto.ServerPersonRespDTO;
import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import cn.iocoder.yudao.module.member.service.serverperson.ServerPersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.SERVER_PERSON_NOT_EXISTS;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.SERVER_PERSON_STATUS_ERROR;

/**
 * @author whycode
 * @title: ServerPersonApiImpl
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/299:01
 */

@Service
public class ServerPersonApiImpl implements ServerPersonApi{

    @Resource
    private ServerPersonService serverPersonService;

    @Override
    public ServerPersonRespDTO getServerPerson(Long id) {
        ServerPersonDO serverPerson = serverPersonService.getServerPerson(id);

        return ServerPersonConvert.INSTANCE.convert02(serverPerson)
                .setAge(IdcardUtil.getAgeByIdCard(serverPerson.getIdCard()));
    }
    @Override
    public ServerPersonRespDTO validateServerPerson(Long id, Long userId) {
        ServerPersonDO serverPerson = serverPersonService.getServerPerson(id);
        if(serverPerson == null){
            throw exception(SERVER_PERSON_NOT_EXISTS);
        }/*else if(CommonStatusEnum.OPEN.name().equals(serverPerson.getStatus())){
            throw exception(SERVER_PERSON_STATUS_ERROR);
        }*/else if(userId != null && !userId.equals(serverPerson.getMemberId())){
            throw exception(SERVER_PERSON_NOT_EXISTS);
        }
        return ServerPersonConvert.INSTANCE.convert02(serverPerson)
                .setAge(IdcardUtil.getAgeByIdCard(serverPerson.getIdCard()));
    }

    @Override
    public Map<Long, ServerPersonRespDTO> getServerPersonMap(Set<Long> ids) {
        List<ServerPersonDO> serverPersonList = serverPersonService.getServerPersonList(ids);
        List<ServerPersonRespDTO> dtoList =ServerPersonConvert.INSTANCE.convertList02(serverPersonList);
        return convertMap(dtoList, ServerPersonRespDTO::getId);
    }
}
