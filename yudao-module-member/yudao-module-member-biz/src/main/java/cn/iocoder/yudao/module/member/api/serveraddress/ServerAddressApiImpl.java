package cn.iocoder.yudao.module.member.api.serveraddress;


import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.ServerAddressRespVO;
import cn.iocoder.yudao.module.member.convert.serveraddress.ServerAddressConvert;
import cn.iocoder.yudao.module.member.service.serveraddress.ServerAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ServerAddressApiImpl implements ServerAddressApi {

    @Resource
    private ServerAddressService serverAddressService;
    @Override
    public ServerAddressRespVO getServerAddressRespVO(Long id, Long userId) {
        return ServerAddressConvert.INSTANCE.convert(serverAddressService.getServerAddress4Api(id,userId));
    }
}
