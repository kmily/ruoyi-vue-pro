package cn.iocoder.yudao.module.system.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductRespVO extends ProductBaseVO {

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
