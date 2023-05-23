package com.cw.module.trade.controller.admin.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 交易账号更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountUpdateReqVO extends AccountBaseVO {

    @Schema(description = "主键ID", required = true, example = "23234")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
