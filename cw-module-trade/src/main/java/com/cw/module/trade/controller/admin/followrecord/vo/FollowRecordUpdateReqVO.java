package com.cw.module.trade.controller.admin.followrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 账号跟随记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FollowRecordUpdateReqVO extends FollowRecordBaseVO {

    @Schema(description = "主键ID", required = true, example = "31944")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
