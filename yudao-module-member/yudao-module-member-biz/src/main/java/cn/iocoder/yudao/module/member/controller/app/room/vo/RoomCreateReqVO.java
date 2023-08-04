package cn.iocoder.yudao.module.member.controller.app.room.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 用户房间创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomCreateReqVO extends RoomBaseVO {

}
