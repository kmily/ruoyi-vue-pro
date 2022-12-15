package cn.iocoder.yudao.module.product.controller.admin.property.vo.value;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(title = "管理后台 - 规格名称值分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductPropertyValuePageReqVO extends PageParam {

    @Schema(title = "规格id", example = "1024")
    private String propertyId;

    @Schema(title = "规格值", example = "红色")
    private String name;

    @Schema(title = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1", description = "参见 CommonStatusEnum 枚举")
    private Integer status;

}
