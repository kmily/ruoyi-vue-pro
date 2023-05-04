package cn.iocoder.yudao.module.oa.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 产品更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductUpdateReqVO extends ProductBaseVO {

    @Schema(description = "id", required = true, example = "633")
    @NotNull(message = "id不能为空")
    private Long id;

}
