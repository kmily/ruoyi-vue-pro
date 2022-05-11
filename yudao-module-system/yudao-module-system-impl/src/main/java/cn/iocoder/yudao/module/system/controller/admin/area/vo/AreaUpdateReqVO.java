package cn.iocoder.yudao.module.system.controller.admin.area.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 行政区域更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaUpdateReqVO extends AreaBaseVO {

    @ApiModelProperty(value = "自增主键id", required = true)
    @NotNull(message = "自增主键id不能为空")
    private Long id;

}
