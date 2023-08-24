package cn.iocoder.yudao.module.member.api.deviceuser.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: DeviceUserDTO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1411:21
 */

@Data
public class DeviceUserDTO {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 保活时间
     */
    private LocalDateTime keepalive;

}
