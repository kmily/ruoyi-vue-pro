package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author kemengkai
 * @create 2023-02-13 17:11
 */
@Schema(description = "管理后台 - 可选择的回退任务 Response VO")
@Data
public class BpmBackTaskRespVO {

    @Schema(description = "任务标识")
    private String taskDefKey;

    @Schema(description = "任务名称")
    private String taskDefName;
}
