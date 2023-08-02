package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 砍价更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BargainActivityUpdateReqVO extends BargainActivityBaseVO {

    @Schema(description = "砍价活动编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "砍价活动编号不能为空")
    private Long id;

    @Schema(description = "关联商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25326")
    @NotNull(message = "关联商品ID不能为空")
    private Long spuId;

    @Schema(description = "砍价商品表skuID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19739")
    @NotNull(message = "砍价商品表skuID不能为空")
    private Long bargainSkuId;

}
