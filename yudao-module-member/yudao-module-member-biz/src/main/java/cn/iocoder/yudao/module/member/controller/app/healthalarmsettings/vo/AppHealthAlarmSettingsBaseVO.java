package cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 体征检测雷达设置 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppHealthAlarmSettingsBaseVO {

    @Schema(description = "设备ID", required = true, example = "13261")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    @Schema(description = "心率异常告警")
    private Boolean heart;

    @Schema(description = "正常心率范围", required = true, example = "60-100")
    private String heartRange;

    @Schema(description = "呼吸告警设置")
    private Boolean breathe;

    @Schema(description = "正常心率范围", required = true, example = "16-24")
    private String breatheRange;

    @Schema(description = "体动检测告警")
    private Boolean move;

    @Schema(description = "坐起告警")
    private Boolean sitUp;

    @Schema(description = "离床告警")
    private Boolean outBed;

}
