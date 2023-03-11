package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

// TODO @ke: 看看这个 VO 类，看看是不是都是必须传递的参数。看着有些是可以通过数据源查询到的。
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BpmTaskBackReqVO {

    @Schema(description = "流程编号", required = true, example = "730da750-cc4f-11ec-b58e-1e429355e4a0")
    @NotEmpty(message = "流程编号不能为空")
    private String procInstId;

    @Schema(description = "当前任务编号", required = true, example = "730da750-cc4f-11ec-b58e-1e429355e4a0")
    @NotEmpty(message = "当前任务编号不能为空")
    private String taskId;

    @Schema(description = "当前流程任务标识", required = true, example = "Activity_1jlembv")
    @NotNull(message = "当前流程任务标识不能为空")
    private String oldTaskDefKey;

    @Schema(description = "准备回退的流程任务标识", required = true, example = "Activity_3fds3f")
    @NotNull(message = "准备回退流程任务标识不能为空")
    private String newTaskDefKey;

    // TODO 是否必传哈，如果是的话，@NotNull 注解
    @Schema(description = "审批结果", required = true, example = "任务驳回")
    private String reason;

}
