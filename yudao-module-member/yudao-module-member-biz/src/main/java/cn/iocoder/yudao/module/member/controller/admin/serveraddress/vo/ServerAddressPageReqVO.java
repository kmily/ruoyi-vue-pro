package cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 服务地址分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ServerAddressPageReqVO extends PageParam {

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
