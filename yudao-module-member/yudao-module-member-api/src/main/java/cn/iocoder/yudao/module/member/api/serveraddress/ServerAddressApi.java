package cn.iocoder.yudao.module.member.api.serveraddress;


import cn.iocoder.yudao.module.member.api.serveraddress.dto.ServerAddressApiDTO;

public interface ServerAddressApi {
    ServerAddressApiDTO getServerAddressApiDTO(Long id, Long userId);
}
