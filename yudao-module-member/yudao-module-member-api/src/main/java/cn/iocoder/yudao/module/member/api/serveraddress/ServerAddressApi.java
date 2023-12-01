package cn.iocoder.yudao.module.member.api.serveraddress;

import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.ServerAddressRespVO;

public interface ServerAddressApi {
    ServerAddressRespVO getServerAddressRespVO(Long id, Long userId);
}
