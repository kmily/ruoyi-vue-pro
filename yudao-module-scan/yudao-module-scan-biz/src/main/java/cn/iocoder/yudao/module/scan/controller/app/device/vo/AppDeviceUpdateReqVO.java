package cn.iocoder.yudao.module.scan.controller.app.device.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("扫描APP - 设备更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceUpdateReqVO extends AppDeviceBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "10328")
    @NotNull(message = "id不能为空")
    private Long id;

}
