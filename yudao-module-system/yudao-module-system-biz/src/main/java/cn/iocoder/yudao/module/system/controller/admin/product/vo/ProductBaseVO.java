package cn.iocoder.yudao.module.system.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 产品 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ProductBaseVO {

    @Schema(description = "产品编码", required = true)
    @NotNull(message = "产品编码不能为空")
    private String productCode;

    @Schema(description = "产品型号", required = true)
    @NotNull(message = "产品型号不能为空")
    private String productModel;

    @Schema(description = "单价", example = "13061")
    private BigDecimal price;

    @Schema(description = "底价", example = "14434")
    private BigDecimal reservePrice;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "产品类型", required = true, example = "1")
    @NotNull(message = "产品类型不能为空")
    private String productType;

    @Schema(description = "单位")
    private String productUnit;

}
