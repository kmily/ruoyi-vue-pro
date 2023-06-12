package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 商机-跟进日志 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OpportunityFollowLogRespVO extends OpportunityFollowLogBaseVO {

    @Schema(description = "id", required = true, example = "4028")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
