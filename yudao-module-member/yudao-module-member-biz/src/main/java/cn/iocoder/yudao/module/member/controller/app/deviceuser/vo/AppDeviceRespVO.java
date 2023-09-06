package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author whycode
 * @title: AppDeviceVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/414:00
 */

@Schema(description = "用户 APP - 设备信息 Response VO")
@Data
public class AppDeviceRespVO {

    @Schema(description = "自增编号", required = true, example = "32074")
    private Long id;

    @Schema(description = "设备ID", required = true)
    private Long deviceId;

    @Schema(description = "设备名称", required = true, example = "跌倒雷达")
    private String name;

    @Schema(description = "自定义名称", required = true, example = "主卧跌倒雷达")
    private String customName;

    @Schema(description = "设备类型", required = true, example = "32074")
    private Integer type;

    @Schema(description = "设备编号", required = true, example = "1000000")
    private String sn;

    @Schema(description = "在线状态", required = true, example = "true")
    private Boolean onLine;

}
