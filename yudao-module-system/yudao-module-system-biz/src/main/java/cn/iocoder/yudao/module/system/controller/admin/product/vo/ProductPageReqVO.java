package cn.iocoder.yudao.module.system.controller.admin.product.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductPageReqVO extends PageParam {

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "产品类型", example = "1")
    private String productType;

}
