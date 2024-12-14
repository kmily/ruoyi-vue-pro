package cn.iocoder.yudao.module.haoka.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 好卡案例新增/修改 Request VO")
@Data
public class HaokaDemoSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "28384")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "名字不能为空")
    private String name;

    @Schema(description = "年龄", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "性别不能为空")
    private Integer agent;

}