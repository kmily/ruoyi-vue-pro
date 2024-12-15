package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品区域配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductLimitAreaPageReqVO extends PageParam {

    @Schema(description = "产品限制ID", example = "22058")
    private Long haokaProductLimitId;

    @Schema(description = "地区")
    private Integer addressCode;

    @Schema(description = "地区", example = "张三")
    private String addressName;

    @Schema(description = "是否允许")
    private Boolean allowed;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}