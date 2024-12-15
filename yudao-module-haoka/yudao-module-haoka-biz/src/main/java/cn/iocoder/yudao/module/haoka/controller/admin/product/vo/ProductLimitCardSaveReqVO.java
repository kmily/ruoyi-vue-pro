package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 产品身份证限制新增/修改 Request VO")
@Data
public class ProductLimitCardSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16180")
    private Long id;

    @Schema(description = "产品限制ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12808")
    @NotNull(message = "产品限制ID不能为空")
    private Long haokaProductLimitId;

    @Schema(description = "身份证号前4或6位")
    private Integer cardNum;

}