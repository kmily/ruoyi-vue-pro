package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// TODO @ke：感觉这里的返回，是不是返回一个通用的 TaskRespVO 就好了。类似 BpmTaskTodoPageItemRespVO 里的字段
@Schema(description = "管理后台 - 可选择的回退任务 Response VO")
@Data
public class BpmBackTaskRespVO {

    @Schema(description = "任务标识")
    private String taskDefKey;

    @Schema(description = "任务名称")
    private String taskDefName;
}
