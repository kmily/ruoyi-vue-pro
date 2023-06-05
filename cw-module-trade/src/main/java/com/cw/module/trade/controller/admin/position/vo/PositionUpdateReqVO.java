package com.cw.module.trade.controller.admin.position.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 账户持仓信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionUpdateReqVO extends PositionBaseVO {

    @Schema(description = "主键ID", required = true, example = "9514")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
