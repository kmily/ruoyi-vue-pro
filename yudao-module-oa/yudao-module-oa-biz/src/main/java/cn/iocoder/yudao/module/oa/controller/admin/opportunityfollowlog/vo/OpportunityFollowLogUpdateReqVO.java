package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 商机-跟进日志更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OpportunityFollowLogUpdateReqVO extends OpportunityFollowLogBaseVO {

    @Schema(description = "id", required = true, example = "4028")
    @NotNull(message = "id不能为空")
    private Long id;

}
