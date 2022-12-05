package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(title = "管理后台 - 流程模型更新状态 Request VO")
@Data
public class BpmModelUpdateStateReqVO {

    @Schema(title  = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private String id;

    @Schema(title  = "状态", required = true, example = "1", description = "见 SuspensionState 枚举")
    @NotNull(message = "状态不能为空")
    private Integer state;

}
