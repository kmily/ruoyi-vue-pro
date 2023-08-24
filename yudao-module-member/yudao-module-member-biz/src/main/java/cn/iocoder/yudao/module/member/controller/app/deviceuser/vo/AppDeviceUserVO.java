package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author whycode
 * @title: AppDeviceUserVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/414:47
 */

@Schema(description = "用户 APP - 设备和用户绑定 Response VO")
@Data
public class AppDeviceUserVO {

    @Schema(description = "编号", required = true, example = "1")
    private Long id;

    @Schema(description = "房间ID", required = true, example = "7201")
    private Long roomId;

    @Schema(description = "房间名称", required = true, example = "客厅")
    private String roomName;

    @Schema(description = "设备ID", required = true, example = "1")
    private Long deviceId;

    @Schema(description = "设备名称", required = true, example = "跌倒雷达")
    private String deviceName;

    @Schema(description = "自定义名称", required = true, example = "主卧跌倒雷达")
    private String customName;

    @Schema(description = "设备类型", required = true, example = "1")
    private Integer type;

    @Schema(description = "设备编号", required = true, example = "1000000")
    private String sn;

    @Schema(description = "是否在线", required = true, example = "true")
    private Boolean onLine;


}
