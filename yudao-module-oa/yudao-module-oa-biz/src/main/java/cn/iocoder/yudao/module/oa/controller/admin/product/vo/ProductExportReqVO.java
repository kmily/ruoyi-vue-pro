package cn.iocoder.yudao.module.oa.controller.admin.product.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品 Excel 导出 Request VO，参数和 ProductPageReqVO 是一致的")
@Data
public class ProductExportReqVO {

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "产品类型", example = "1")
    private String productType;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
