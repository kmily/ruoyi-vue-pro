package cn.iocoder.yudao.module.scan.controller.app.device.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("扫描APP - 设备创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceCreateReqVO extends AppDeviceBaseVO {

}
