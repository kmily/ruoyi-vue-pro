package cn.iocoder.yudao.module.product.controller.admin.category.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 商品分类更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductCategoryUpdateReqVO extends ProductCategoryBaseVO {

    @ApiModelProperty(value = "分类编号", required = true, example = "2")
    @NotNull(message = "分类编号不能为空")
    private Long id;

}
