package cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 绊线数据 Excel 导出 Request VO，参数和 LineRuleDataPageReqVO 是一致的")
@Data
public class LineRuleDataExportReqVO {

    @Schema(description = "设备ID", example = "17905")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "上报时间")
    private Long timeStamp;

    @Schema(description = "消息序号。用于判定数据连续性")
    private Integer seq;

    @Schema(description = "绊线规则数量")
    private Integer lineNum;

    @Schema(description = "绊线统计数据")
    private String lineData;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
