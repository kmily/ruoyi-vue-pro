package cn.iocoder.yudao.module.radar.api.device.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author whycode
 * @title: DeviceDTO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/214:07
 */

@Data
@Accessors(chain = true)
public class DeviceDTO {

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备编号
     */
    private String sn;

    /**
     * 设备类型
     */
    private Integer type;

    /**
     * 多租户编号
     */
    private Long tenantId;

    /**
     * 设备名称
     */
    private String name;

}
