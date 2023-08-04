package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 设备和用户绑定 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceUserRespVO extends AppDeviceUserBaseVO {

    @Schema(description = "自增编号", required = true, example = "7201")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

    @Schema(description = "房间名称", required = true)
    private String name;

    @Schema(description = "房间下的设备", required = true)
    private List<AppDeviceRespVO> deviceRespVOList;

}
