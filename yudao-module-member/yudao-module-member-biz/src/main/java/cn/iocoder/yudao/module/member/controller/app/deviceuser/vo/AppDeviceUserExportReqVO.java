package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 设备和用户绑定 Excel 导出 Request VO，参数和 DeviceUserPageReqVO 是一致的")
@Data
public class AppDeviceUserExportReqVO {

    @Schema(description = "用户ID", example = "24626")
    private Long userId;

    @Schema(description = "设备ID", example = "26138")
    private Long deviceId;

    @Schema(description = "家庭ID", example = "14529")
    private Long familyId;

    @Schema(description = "房间ID", example = "25807")
    private Long roomId;

    @Schema(description = "自定义设备名称", example = "张三")
    private String customName;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
