package cn.iocoder.yudao.module.system.controller.admin.area.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 行政区域 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaRespVO extends AreaBaseVO {

    @ApiModelProperty(value = "自增主键id", required = true)
    private Long id;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

}
