package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 设备通知创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceNoticeCreateReqVO extends AppDeviceNoticeBaseVO {

}
