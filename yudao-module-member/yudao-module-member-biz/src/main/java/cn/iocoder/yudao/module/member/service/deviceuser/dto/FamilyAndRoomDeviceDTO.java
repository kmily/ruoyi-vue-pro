package cn.iocoder.yudao.module.member.service.deviceuser.dto;

import lombok.Data;

/**
 * @author whycode
 * @title: DeviceUserDTO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2113:50
 */

@Data
public class FamilyAndRoomDeviceDTO {

    private Long deviceId;

    private String family;

    private String room;

    private String phones;

    private Long userId;
}
