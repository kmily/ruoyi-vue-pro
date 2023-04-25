package cn.iocoder.yudao.module.system.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 客户管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerUpdateReqVO extends CustomerBaseVO {

    @Schema(description = "id", required = true, example = "25163")
    @NotNull(message = "id不能为空")
    private Long id;

}
