package cn.iocoder.yudao.module.db.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 物料新增/修改 Request VO")
@Data
public class MaterialSaveReqVO {

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
    private LocalDateTime checkTime;

    @Schema(description = "单位内码", requiredMode = Schema.RequiredMode.REQUIRED, example = "16111")
    @NotEmpty(message = "单位内码不能为空")
    private String unitId;

    @Schema(description = "审核人")
    private String checker;

    @Schema(description = "唯一ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13878")
    private String id;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "编码不能为空")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "物料类型不能为空")
    private String type;

    @Schema(description = "ABC分类")
    private String abc;

}