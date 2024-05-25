package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "治疗交互 - 获取下一个 Response VO")
@Data
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetNextRespVO {

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "guide")
    private String msgType;


    @Schema(description = "业务类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "goal_and_motivation")
    private String serviceCategory; // 主流程，治疗子任务， 问卷

    @Schema(description = "业务子类别", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "xxx")
    private String serviceSubCategory; // TODO

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "7380")
    private Long id;

    @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Object> data;

    @Schema(description = "提交数据路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String submitPath;
}
