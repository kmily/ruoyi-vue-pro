package cn.iocoder.yudao.module.pay.controller.admin.app.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "管理后台 - 支付应用信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayAppCreateReqVO extends PayAppBaseVO {

    @Schema(description = "应用编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotNull(message = "应用编码不能为空")
    private String appKey;

}
