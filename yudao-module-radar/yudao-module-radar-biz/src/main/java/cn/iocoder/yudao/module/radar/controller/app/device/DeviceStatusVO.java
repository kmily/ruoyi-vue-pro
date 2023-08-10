package cn.iocoder.yudao.module.radar.controller.app.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author whycode
 * @title: DeviceStatusVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/910:50
 */
@Schema(description = "用户 APP - 设备状态 Response VO")
@Data
@ToString(callSuper = true)
public class DeviceStatusVO {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备状态，0-离线，1-在线")
    private Integer status;

}
