package cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品对接上游配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperiorProductConfigPageReqVO extends PageParam {

    @Schema(description = "ID", example = "7050")
    private Long haokaSuperiorApiId;

    @Schema(description = "ID", example = "26555")
    private Long haokaProductId;

    @Schema(description = "是否已配置")
    private Boolean isConfined;

    @Schema(description = "值")
    private String config;

    @Schema(description = "是否必填")
    private Boolean required;

    @Schema(description = "说明")
    private String remarks;

    @Schema(description = "部门ID", example = "30274")
    private Long deptId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}