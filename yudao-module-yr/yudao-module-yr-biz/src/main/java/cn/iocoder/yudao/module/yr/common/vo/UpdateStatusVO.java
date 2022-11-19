package cn.iocoder.yudao.module.yr.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 状态更新 Request VO")
@Data
@ToString(callSuper = true)
public class UpdateStatusVO {

    @ApiModelProperty(value = "主键", required = true,example="1")
    @NotNull(message = "不能为空")
    private Long id;

    @ApiModelProperty(value = "状态 （0正常 1停用）", required = true,example="1")
    @NotNull(message = "状态 （0正常 1停用）不能为空")
    private Integer status;

}
