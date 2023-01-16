package cn.iocoder.yudao.module.scan.controller.admin.users.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 扫描用户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsersUpdateReqVO extends UsersBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "27710")
    @NotNull(message = "id不能为空")
    private Long id;

}
