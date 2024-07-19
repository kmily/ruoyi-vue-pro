package cn.iocoder.yudao.module.system.controller.admin.codingrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 编号规则表头 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CodingRulesRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19250")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "规则编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("规则编码")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "描述", example = "随便")
    @ExcelProperty("描述")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
