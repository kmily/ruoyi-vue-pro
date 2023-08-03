package cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备和用户绑定 Excel 导出 Request VO，参数和 DeviceUserPageReqVO 是一致的")
@Data
public class DeviceUserExportReqVO {

    @Schema(description = "用户ID", example = "7707")
    private Long userId;

    @Schema(description = "设备ID", example = "24701")
    private Long deviceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
