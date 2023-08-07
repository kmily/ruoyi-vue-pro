package cn.iocoder.yudao.module.radar.controller.admin.banner.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 雷达模块banner图 Excel 导出 Request VO，参数和 BannerPageReqVO 是一致的")
@Data
public class BannerExportReqVO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "跳转连接", example = "https://www.iocoder.cn")
    private String url;

    @Schema(description = "图片连接", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "排序")
    private Byte sort;

    @Schema(description = "部门状态（0正常 1停用）", example = "2")
    private Byte status;

    @Schema(description = "备注", example = "随便")
    private String memo;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
