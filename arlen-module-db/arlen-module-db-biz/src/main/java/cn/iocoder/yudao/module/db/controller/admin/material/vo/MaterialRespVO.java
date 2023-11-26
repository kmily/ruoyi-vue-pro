package cn.iocoder.yudao.module.db.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 物料 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialRespVO {

    @Schema(description = "组织ID", example = "40")
    @ExcelProperty("组织ID")
    private String orgId;

    @Schema(description = "ERP物料编码", example = "7778")
    @ExcelProperty("ERP物料编码")
    private String erpMtrlId;

    @Schema(description = "规格")
    @ExcelProperty("规格")
    private String model;

    @Schema(description = "仓库ID", example = "15456")
    @ExcelProperty("仓库ID")
    private String storeId;

    @Schema(description = "储位ID", example = "14470")
    @ExcelProperty("储位ID")
    private String storelocId;

    @Schema(description = "最低安全库存")
    @ExcelProperty("最低安全库存")
    private BigDecimal qtySkMin;

    @Schema(description = "最高安全库存")
    @ExcelProperty("最高安全库存")
    private BigDecimal qtySkMax;

    @Schema(description = "预警库存")
    @ExcelProperty("预警库存")
    private BigDecimal qtySkWarn;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String rem;

    @Schema(description = "状态", example = "2")
    @ExcelProperty("状态")
    private String status;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime checkTime;

    @Schema(description = "单位内码", requiredMode = Schema.RequiredMode.REQUIRED, example = "16111")
    @ExcelProperty("单位内码")
    private String unitId;

    @Schema(description = "审核人")
    @ExcelProperty("审核人")
    private String checker;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "唯一ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13878")
    @ExcelProperty("唯一ID")
    private String id;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "物料类型", converter = DictConvert.class)
    @DictFormat("db_mtrl_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String type;

    @Schema(description = "ABC分类")
    @ExcelProperty(value = "ABC分类", converter = DictConvert.class)
    @DictFormat("db_mtrl_abc") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String abc;

}