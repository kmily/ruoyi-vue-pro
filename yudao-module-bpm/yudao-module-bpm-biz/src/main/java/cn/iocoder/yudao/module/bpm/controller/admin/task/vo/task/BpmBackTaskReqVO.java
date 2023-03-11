package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// TODO @ke: 看看这个 VO 类，看看是不是都是必须传递的参数。看着有些是可以通过数据源查询到的。
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
