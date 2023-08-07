package cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 人体检测雷达设置 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppDetectionAlarmSettingsBaseVO {

    @Schema(description = "设备ID", required = true, example = "5914")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    @Schema(description = "进入告警")
    private Boolean enter;

    @Schema(description = "离开告警")
    private Boolean leave;

}
