package cn.iocoder.yudao.module.product.controller.admin.brand.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "管理后台 - 商品品牌分页 Request VO")
@Data
public class ProductBrandListReqVO {

    @Schema(title  = "品牌名称", example = "芋道")
    private String name;

}
