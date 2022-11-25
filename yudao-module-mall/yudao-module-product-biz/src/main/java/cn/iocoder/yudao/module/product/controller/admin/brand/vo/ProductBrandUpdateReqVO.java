package cn.iocoder.yudao.module.product.controller.admin.brand.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 商品品牌更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductBrandUpdateReqVO extends ProductBrandBaseVO {

    @ApiModelProperty(value = "品牌编号", required = true, example = "1")
    @NotNull(message = "品牌编号不能为空")
    private Long id;

}
