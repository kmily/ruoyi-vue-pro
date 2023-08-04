package cn.iocoder.yudao.module.member.controller.app.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 用户房间 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomRespVO extends RoomBaseVO {

    @Schema(description = "自增编号", required = true, example = "32074")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

    @Schema(description = "房间包含设备")
    private List<RoomDeviceVO> roomDeviceVOList;
}
