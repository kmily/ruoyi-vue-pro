package cn.iocoder.yudao.module.product.controller.admin.property.vo.property;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 规格名称 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ProductPropertyBaseVO {

    @Schema(title  = "规格名称", required = true, example = "颜色")
    @NotBlank(message = "规格名称不能为空")
    private String name;

    @Schema(title  = "备注", example = "颜色")
    private String remark;

    @Schema(title  = "状态", required = true, example = "1", description = "参见 CommonStatusEnum 枚举")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
