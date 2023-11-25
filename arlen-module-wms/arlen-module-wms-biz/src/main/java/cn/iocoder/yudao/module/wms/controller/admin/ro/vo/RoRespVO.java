package cn.iocoder.yudao.module.wms.controller.admin.ro.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 收料单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RoRespVO {

    @Schema(description = "收料单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("收料单号")
    private String roCode;

    @Schema(description = "收料类型")
    @ExcelProperty(value = "收料类型", converter = DictConvert.class)
    @DictFormat("wms_ro_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String roType;

    @Schema(description = "收料状态")
    @ExcelProperty(value = "收料状态", converter = DictConvert.class)
    @DictFormat("wms_ro_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String roStatus;

    @Schema(description = "加急")
    @ExcelProperty("加急")
    private String isUrgent;

    @Schema(description = "ASN")
    @ExcelProperty("ASN")
    private String asn;

    @Schema(description = "供应商ID", example = "3418")
    @ExcelProperty("供应商ID")
    private String supId;

    @Schema(description = "客户ID", example = "31016")
    @ExcelProperty("客户ID")
    private String custId;

    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22020")
    @ExcelProperty("部门ID")
    private String deptId;

    @Schema(description = "员工ID", example = "5408")
    @ExcelProperty("员工ID")
    private String empId;

    @Schema(description = "来源")
    @ExcelProperty(value = "来源", converter = DictConvert.class)
    @DictFormat("com_data_src") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String rosrcType;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime checkTime;

    @Schema(description = "关闭")
    @ExcelProperty("关闭")
    private String closed;

    @Schema(description = "组织ID", example = "31660")
    @ExcelProperty("组织ID")
    private Long orgId;

    @Schema(description = "审核人ID")
    @ExcelProperty("审核人ID")
    private String checker;

    @Schema(description = "关闭人")
    @ExcelProperty("关闭人")
    private String closer;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}