package cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 提现银行卡更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareBankCardUpdateReqVO extends CareBankCardBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2483")
    @NotNull(message = "ID不能为空")
    private Long id;

}
