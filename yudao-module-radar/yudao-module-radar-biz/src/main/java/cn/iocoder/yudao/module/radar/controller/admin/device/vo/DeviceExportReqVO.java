package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备信息 Excel 导出 Request VO，参数和 DevicePageReqVO 是一致的")
@Data
public class DeviceExportReqVO {

    @Schema(description = "设备sn编号")
    private String sn;

    @Schema(description = "设备名称", example = "李四")
    private String name;

    @Schema(description = "设备类别", example = "1")
    private Integer type;

    @Schema(description = "部门状态（0正常 1停用）", example = "2")
    private Byte status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
