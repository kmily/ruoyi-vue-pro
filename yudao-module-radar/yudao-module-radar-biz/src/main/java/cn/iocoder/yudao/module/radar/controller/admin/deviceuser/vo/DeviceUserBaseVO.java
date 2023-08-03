package cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo;

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
public class DeviceUserBaseVO {

    @Schema(description = "用户ID", required = true, example = "7707")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "设备ID", required = true, example = "24701")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

}
