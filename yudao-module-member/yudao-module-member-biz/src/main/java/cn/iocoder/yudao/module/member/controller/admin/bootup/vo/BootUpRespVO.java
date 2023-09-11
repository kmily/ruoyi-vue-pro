package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户启动数据 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BootUpRespVO extends BootUpBaseVO {

    @Schema(description = "主键", required = true, example = "11699")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
