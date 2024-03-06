package cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户查询明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YouyouDetailsPageReqVO extends PageParam {

    @Schema(description = "通过申请获取的AppKey", example = "123456")
    private String appkey;

    @Schema(description = "时间戳", example = "2016-01-01 12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "API输入参数签名结果")
    private String sign;

    @Schema(description = "明细类型，1=订单明细，2=资金流水", example = "1")
    private Integer dataType;

    @Schema(description = "开始时间", example = "2016-01-01 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "结束时间", example = "2016-01-01 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "申请标识")
    private String applyCode;

    @Schema(description = "查询明细结果返回的url")
    private String data;

}