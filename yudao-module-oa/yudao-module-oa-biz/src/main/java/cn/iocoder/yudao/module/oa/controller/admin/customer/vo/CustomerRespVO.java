package cn.iocoder.yudao.module.oa.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 客户管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerRespVO extends CustomerBaseVO {

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "id", required = true, example = "25163")
    private Long id;

}
