package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Banner Excel 导出 Request VO，参数和 BannerPageReqVO 是一致的")
@Data
public class BannerExportReqVO {

    @Schema(description = "Banner 标题")
    private String title;

    @Schema(description = "活动状态", example = "1")
    private Byte status;

    @Schema(description = "位置")
    private Byte position;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
