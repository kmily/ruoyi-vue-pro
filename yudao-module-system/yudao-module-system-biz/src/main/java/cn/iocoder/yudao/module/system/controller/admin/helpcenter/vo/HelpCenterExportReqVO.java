package cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 常见问题 Excel 导出 Request VO，参数和 HelpCenterPageReqVO 是一致的")
@Data
public class HelpCenterExportReqVO {

    @Schema(description = "帮助类型", example = "2")
    private Byte type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问题描述", example = "随便")
    private String description;

    @Schema(description = "状态（0正常 1禁用）", example = "2")
    private Byte status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
