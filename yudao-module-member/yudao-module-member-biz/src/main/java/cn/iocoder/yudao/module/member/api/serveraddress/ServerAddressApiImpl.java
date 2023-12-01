package cn.iocoder.yudao.module.member.api.serveraddress;


import cn.iocoder.yudao.module.member.api.serveraddress.dto.ServerAddressApiDTO;
import cn.iocoder.yudao.module.member.convert.serveraddress.ServerAddressConvert;
import cn.iocoder.yudao.module.member.service.serveraddress.ServerAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ServerAddressApiImpl implements ServerAddressApi {

    @Resource
    private ServerAddressService serverAddressService;
    @Override
    public ServerAddressApiDTO getServerAddressApiDTO(Long id, Long userId) {
        return ServerAddressConvert.INSTANCE.convert2RespDTO(serverAddressService.getServerAddressApiDTO(id,userId));
    }
}
