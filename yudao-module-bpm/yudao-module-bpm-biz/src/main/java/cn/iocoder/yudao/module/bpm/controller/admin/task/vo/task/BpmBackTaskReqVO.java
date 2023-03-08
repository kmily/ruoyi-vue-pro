package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author kemengkai
 * @create 2023-02-13 17:11
 */
@Schema(description = "管理后台 - 可选择的回退任务 Response VO")
@Data
public class BpmBackTaskReqVO {

    @Schema(description = "当前任务标识")
    private String oldTaskDefKey;

    @Schema(description = "当前任务部署id")
    private String procDefId;

    @Schema(description = "当前任务id")
    private String procInstId;
}
