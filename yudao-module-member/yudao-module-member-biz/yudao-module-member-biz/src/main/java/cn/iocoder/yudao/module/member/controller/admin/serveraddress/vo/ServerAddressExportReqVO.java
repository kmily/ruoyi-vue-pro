package cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 服务地址 Excel 导出 Request VO，参数和 ServerAddressPageReqVO 是一致的")
@Data
public class ServerAddressExportReqVO {

    @Schema(description = "用户编号", example = "14625")
    private Long userId;

    @Schema(description = "地区编码", example = "28471")
    private Long areaId;

    @Schema(description = "省市县/区")
    private String address;

    @Schema(description = "收件详细地址")
    private String detailAddress;

    @Schema(description = "是否默认", example = "1")
    private Boolean defaultStatus;

    @Schema(description = "经纬度")
    private String coordinate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
