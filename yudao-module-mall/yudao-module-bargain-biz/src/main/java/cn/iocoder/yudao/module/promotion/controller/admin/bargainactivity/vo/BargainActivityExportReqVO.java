package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 砍价 Excel 导出 Request VO，参数和 BargainActivityPageReqVO 是一致的")
@Data
public class BargainActivityExportReqVO {

    @Schema(description = "砍价活动名称", example = "张三")
    private String name;

    @Schema(description = "砍价开启时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "砍价结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "砍价状态 0(关闭)  1(开启)", example = "1")
    private Byte status;

    @Schema(description = "用户帮砍的次数", example = "2980")
    private Integer bargainCount;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "用户帮砍的次数")
    private Integer peopleNum;

}
