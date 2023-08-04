package cn.iocoder.yudao.module.member.controller.app.room.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

/**
* 用户房间 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class RoomBaseVO {

    /*@Schema(description = "用户ID", required = true, example = "5148")
    @NotNull(message = "用户ID不能为空")*/
    private Long userId;

    @Schema(description = "家庭ID", required = true, example = "7466")
    @NotNull(message = "家庭ID不能为空")
    private Long familyId;

    @Schema(description = "房间名称", required = true, example = "芋艿")
    @NotNull(message = "房间名称不能为空")
    private String name;

}
