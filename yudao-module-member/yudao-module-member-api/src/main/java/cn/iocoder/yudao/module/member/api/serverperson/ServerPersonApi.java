package cn.iocoder.yudao.module.member.api.serverperson;

import cn.iocoder.yudao.module.member.api.serverperson.dto.ServerPersonRespDTO;

import java.util.Map;
import java.util.Set;

/**
 * @author whycode
 * @title: ServerPersonApi
 * @projectName home-care
 * @description: 被护人 接口
 * @date 2023/11/2816:51
 */
public interface ServerPersonApi {


    /**
     * 根据ID 查询被护人信息
     * @param id 被护人编号
     * @return 返回被护人信息
     */
    ServerPersonRespDTO getServerPerson(Long id);

    /**
     * 校验被护人
     * @param id 被护人编号
     * @param userId 用户编号
     * @return
     */
     ServerPersonRespDTO validateServerPerson(Long id, Long userId);

     Map<Long, ServerPersonRespDTO> getServerPersonMap(Set<Long> ids);
}
