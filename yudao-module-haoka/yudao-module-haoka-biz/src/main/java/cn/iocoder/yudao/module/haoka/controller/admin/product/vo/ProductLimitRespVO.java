package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 产品限制条件 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductLimitRespVO {

    @Schema(description = "产品类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24464")
    @ExcelProperty("产品类型ID")
    private Long id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("产品类型名称")
    private String name;

    @Schema(description = "是否使用只发货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否使用只发货地址")
    private Boolean useOnlySendArea;

    @Schema(description = "是否使用不发货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否使用不发货地址")
    private Boolean useNotSendArea;

    @Schema(description = "是否使用身份证限制", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否使用身份证限制")
    private Boolean useCardLimit;

    @Schema(description = "最大年龄限制")
    @ExcelProperty("最大年龄限制")
    private Integer ageMax;

    @Schema(description = "最小年龄限制")
    @ExcelProperty("最小年龄限制")
    private Integer ageMin;

    @Schema(description = "单人开卡数量限制")
    @ExcelProperty("单人开卡数量限制")
    private Integer personCardQuantityLimit;

    @Schema(description = "检测周期(月)")
    @ExcelProperty("检测周期(月)")
    private Integer detectionCycle;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}