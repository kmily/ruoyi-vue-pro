package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

/**
* 工程实施列 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProjectImplBaseVO {

    @Schema(description = "合同id", required = true, example = "10972")
    @NotNull(message = "合同id不能为空")
    private Long contractId;

    @Schema(description = "实施内容")
    private String implContent;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "实施范围", required = true)
    @NotNull(message = "实施范围不能为空")
    private String implScope;

}
