package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 设备通知 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppDeviceNoticeBaseVO {

    @Schema(description = "用户ID", required = true, example = "31640")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "家庭ID", required = true, example = "32186")
    @NotNull(message = "家庭ID不能为空")
    private Long familyId;

    @Schema(description = "设备ID", required = true, example = "14012")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "卡片名称")
    private String content;

    @Schema(description = "设备类型", example = "1")
    private Byte type;

    @Schema(description = "状态（0未读 1已读）", example = "1")
    private Byte status;

    @Schema(description = "发生时间")
    private LocalDateTime happenTime;

}
