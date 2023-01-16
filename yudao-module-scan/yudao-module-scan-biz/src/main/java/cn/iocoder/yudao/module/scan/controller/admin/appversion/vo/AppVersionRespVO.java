package cn.iocoder.yudao.module.scan.controller.admin.appversion.vo;

import lombok.*;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 应用版本记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppVersionRespVO extends AppVersionBaseVO {

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "id", required = true, example = "22971")
    private Long id;

}
