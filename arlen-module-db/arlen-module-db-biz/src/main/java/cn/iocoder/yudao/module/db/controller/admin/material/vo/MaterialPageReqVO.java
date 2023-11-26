package cn.iocoder.yudao.module.db.controller.admin.material.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialPageReqVO extends PageParam {

    @Schema(description = "组织ID", example = "40")
    private String orgId;

    @Schema(description = "ERP物料编码", example = "7778")
    private String erpMtrlId;

    @Schema(description = "规格")
    private String model;

    @Schema(description = "仓库ID", example = "15456")
    private String storeId;

    @Schema(description = "储位ID", example = "14470")
    private String storelocId;

    @Schema(description = "最低安全库存")
    private BigDecimal qtySkMin;

    @Schema(description = "最高安全库存")
    private BigDecimal qtySkMax;

    @Schema(description = "预警库存")
    private BigDecimal qtySkWarn;

    @Schema(description = "备注")
    private String rem;

    @Schema(description = "状态", example = "2")
    private String status;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkTime;

    @Schema(description = "单位内码", example = "16111")
    private String unitId;

    @Schema(description = "审核人")
    private String checker;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "物料类型", example = "1")
    private String type;

    @Schema(description = "ABC分类")
    private String abc;

}