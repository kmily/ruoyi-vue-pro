package cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 服务地址更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ServerAddressUpdateReqVO extends ServerAddressBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27880")
    @NotNull(message = "ID不能为空")
    private Long id;

}
