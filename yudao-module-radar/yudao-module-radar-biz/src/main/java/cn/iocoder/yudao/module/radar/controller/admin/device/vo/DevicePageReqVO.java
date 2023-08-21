package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevicePageReqVO extends PageParam {

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

    @Schema(description = "绑定状态（0未 1已）", required = true, example = "1")
    private Byte bind;

    @Schema(description = "保活时间", required = true)
    private LocalDateTime keepalive;
    @Schema(description = "绑定时间", required = true)
    private LocalDateTime bindTime;

}
