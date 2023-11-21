package cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 资质信息 Excel 导出 Request VO，参数和 AptitudePageReqVO 是一致的")
@Data
public class AptitudeExportReqVO {

    @Schema(description = "资质名称", example = "李四")
    private String name;

    @Schema(description = "资质图标", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "状态（0正常 1停用）", example = "2")
    private Byte status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
