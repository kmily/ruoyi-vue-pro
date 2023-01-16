package cn.iocoder.yudao.module.scan.controller.admin.appversion.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 应用版本记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppVersionUpdateReqVO extends AppVersionBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "22971")
    @NotNull(message = "id不能为空")
    private Long id;

}
