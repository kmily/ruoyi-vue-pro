package cn.iocoder.yudao.module.scan.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 设备更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUpdateReqVO extends DeviceBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "10328")
    @NotNull(message = "id不能为空")
    private Long id;

}
