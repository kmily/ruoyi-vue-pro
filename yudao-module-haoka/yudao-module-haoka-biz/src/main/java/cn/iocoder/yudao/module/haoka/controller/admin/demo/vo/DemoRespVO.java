package cn.iocoder.yudao.module.haoka.controller.admin.demo.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 好卡案例 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DemoRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21448")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "年龄", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("性别")
    private Integer agent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
