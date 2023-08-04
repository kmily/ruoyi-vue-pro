package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 设备和用户绑定 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppDeviceUserBaseVO {

    @Schema(description = "用户ID", required = false, example = "24626")
    private Long userId;

    //@Schema(description = "设备ID", required = true, example = "26138")
    //@NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "家庭ID", required = true, example = "14529")
    @NotNull(message = "家庭ID不能为空")
    private Long familyId;

    @Schema(description = "房间ID", required = true, example = "25807")
    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @Schema(description = "自定义设备名称", example = "张三")
    private String customName;



}
