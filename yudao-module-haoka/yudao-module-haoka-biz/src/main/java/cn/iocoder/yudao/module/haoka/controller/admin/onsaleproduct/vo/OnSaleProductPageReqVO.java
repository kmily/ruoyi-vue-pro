package cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 在售产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OnSaleProductPageReqVO extends PageParam {

    @Schema(description = "产品", example = "17875")
    private Long parentProductId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商家编码")
    private String sku;

    @Schema(description = "上架")
    private Boolean onSale;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}