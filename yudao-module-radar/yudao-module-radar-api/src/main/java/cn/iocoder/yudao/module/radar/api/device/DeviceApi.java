package cn.iocoder.yudao.module.radar.api.device;

import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author whycode
 * @title: DeviceApi
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/214:05
 */
public interface DeviceApi {

    /**
     * 根据设备编号返回设备
     * @param sn 设备编号
     * @return
     */
    DeviceDTO queryBySn(String sn);

    /**
     * 根据设备ID查询设备信息
     * @param ids 设备ID
     * @return
     */
    List<DeviceDTO> getByIds(Collection<Long> ids);


    /**
     * 绑定或解绑
     * @param id 设备ID
     * @param bind 绑定状态0-未绑定，1-绑定
     */
    void bindOrUnBind(Long id, byte bind);

}
