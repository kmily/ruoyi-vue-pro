package cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 人员存在感知雷达设置创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppExistAlarmSettingsCreateReqVO extends AppExistAlarmSettingsBaseVO {

}
