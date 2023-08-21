package cn.iocoder.yudao.module.member.api.deviceuser;

import cn.iocoder.yudao.module.member.api.deviceuser.dto.DeviceUserDTO;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author whycode
 * @title: DeviceUserApiImpl
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1411:23
 */

@Service
public class DeviceUserApiImpl implements DeviceUserApi{

    @Resource
    private DeviceUserService deviceUserService;

    @Override
    public List<DeviceUserDTO> queryUser(Collection<Long> deviceIds) {
        return deviceUserService.getDeviceUserListByDevices(deviceIds);
    }
}
