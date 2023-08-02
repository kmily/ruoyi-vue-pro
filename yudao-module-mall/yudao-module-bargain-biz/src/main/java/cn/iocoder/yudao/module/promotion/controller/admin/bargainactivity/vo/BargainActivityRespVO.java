package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 砍价 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BargainActivityRespVO extends BargainActivityBaseVO {

    @Schema(description = "砍价活动编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long id;

    @Schema(description = "关联商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25326")
    private Long spuId;

    @Schema(description = "砍价商品表skuID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19739")
    private Long bargainSkuId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
