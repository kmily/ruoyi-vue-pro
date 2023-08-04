package cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 人员存在感知雷达设置 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppExistAlarmSettingsBaseVO {

    @Schema(description = "设备ID", required = true, example = "28742")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "进入告警")
    private Boolean enter;

    @Schema(description = "离开告警")
    private Boolean leave;

    @Schema(description = "人员滞留报警")
    private Boolean stop;

    @Schema(description = "无人报警")
    private Boolean nobody;

    @Schema(description = "滞留时间 单位分钟")
    private Byte stopTime;

    @Schema(description = "无人时间 单位分钟")
    private Byte nobodyTime;
    @Schema(description = "通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;
}
