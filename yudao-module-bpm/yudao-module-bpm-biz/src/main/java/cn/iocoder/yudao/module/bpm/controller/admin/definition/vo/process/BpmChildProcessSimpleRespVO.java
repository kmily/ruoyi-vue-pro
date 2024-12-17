package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.process;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 子流程精简信息 Response VO")
@Data
public class BpmChildProcessSimpleRespVO {

    @Schema(description = "流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "process_yudao")
    private String key;

    @Schema(description = "流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

}
