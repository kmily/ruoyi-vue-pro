package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户启动数据创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BootUpCreateReqVO extends BootUpBaseVO {

}
