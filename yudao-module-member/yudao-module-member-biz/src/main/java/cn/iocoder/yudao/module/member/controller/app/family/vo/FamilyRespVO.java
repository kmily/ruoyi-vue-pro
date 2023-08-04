package cn.iocoder.yudao.module.member.controller.app.family.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户家庭 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FamilyRespVO extends FamilyBaseVO {

    @Schema(description = "自增编号", required = true, example = "11173")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
