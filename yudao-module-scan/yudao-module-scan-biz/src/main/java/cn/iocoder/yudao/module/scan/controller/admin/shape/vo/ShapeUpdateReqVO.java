package cn.iocoder.yudao.module.scan.controller.admin.shape.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 体型更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShapeUpdateReqVO extends ShapeBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "12754")
    @NotNull(message = "id不能为空")
    private Long id;

}
