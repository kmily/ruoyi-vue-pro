package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户启动数据更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BootUpUpdateReqVO extends BootUpBaseVO {

    @Schema(description = "主键", required = true, example = "11699")
    @NotNull(message = "主键不能为空")
    private Long id;

}
