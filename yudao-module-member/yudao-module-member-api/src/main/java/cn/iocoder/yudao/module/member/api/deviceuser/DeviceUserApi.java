package cn.iocoder.yudao.module.member.api.deviceuser;

import cn.iocoder.yudao.module.member.api.deviceuser.dto.DeviceUserDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author whycode
 * @title: DeviceUserApi
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1411:20
 */
public interface DeviceUserApi {

    List<DeviceUserDTO> queryUser(Collection<Long> deviceIds);

}
