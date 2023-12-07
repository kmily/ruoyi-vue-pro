package cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 提现银行卡更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareBankCardAppUpdateReqVO extends CareBankCardAppBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2483")
    @NotNull(message = "ID不能为空")
    private Long id;

}
