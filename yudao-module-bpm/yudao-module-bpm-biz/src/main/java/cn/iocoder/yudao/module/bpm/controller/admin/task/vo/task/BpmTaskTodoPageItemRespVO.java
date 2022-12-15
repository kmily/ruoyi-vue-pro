package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(title = "管理后台 - 流程任务的 Running 进行中的分页项 Response VO")
@Data
public class BpmTaskTodoPageItemRespVO {

    @Schema(title = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(title = "任务名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(title = "接收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime claimTime;

    @Schema(title = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(title = "激活状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1", description = "参见 SuspensionState 枚举")
    private Integer suspensionState;

    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    @Data
    @Schema(title = "流程实例")
    public static class ProcessInstance {

        @Schema(title = "流程实例编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private String id;

        @Schema(title = "流程实例名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
        private String name;

        @Schema(title = "发起人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long startUserId;

        @Schema(title = "发起人的用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
        private String startUserNickname;

        @Schema(title = "流程定义的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
        private String processDefinitionId;

    }

}
