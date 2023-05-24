package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 工程日志列表 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProjectImplLogBaseVO {

    @Schema(description = "内容")
    private String logContent;

    @Schema(description = "合同id", required = true, example = "1993")
    @NotNull(message = "合同id不能为空")
    private Long contractId;

    @Schema(description = "工程进度", example = "1")
    private String implStatus;

    @Schema(description = "创建者")
    private String creator;

}
