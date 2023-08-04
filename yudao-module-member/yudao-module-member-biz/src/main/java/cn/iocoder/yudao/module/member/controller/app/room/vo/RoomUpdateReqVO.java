package cn.iocoder.yudao.module.member.controller.app.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户房间更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomUpdateReqVO extends RoomBaseVO {

    @Schema(description = "自增编号", required = true, example = "32074")
    @NotNull(message = "自增编号不能为空")
    private Long id;

}
