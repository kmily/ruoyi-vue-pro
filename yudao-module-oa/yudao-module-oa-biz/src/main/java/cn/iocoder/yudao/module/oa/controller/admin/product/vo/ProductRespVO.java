package cn.iocoder.yudao.module.oa.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductRespVO extends ProductBaseVO {

    @Schema(description = "id", required = true, example = "1024")
    private long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
