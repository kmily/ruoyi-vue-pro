package cn.iocoder.yudao.module.product.controller.admin.property.vo.value;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 规格名称值分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductPropertyValuePageReqVO extends PageParam {

    @ApiModelProperty(value = "规格id", example = "1024")
    private String propertyId;

    @ApiModelProperty(value = "规格值", example = "红色")
    private String name;

    @ApiModelProperty(value = "状态", required = true, example = "1", notes = "参见 CommonStatusEnum 枚举")
    private Integer status;

}
