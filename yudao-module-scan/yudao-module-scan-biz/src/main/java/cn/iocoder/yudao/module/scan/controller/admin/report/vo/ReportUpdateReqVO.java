package cn.iocoder.yudao.module.scan.controller.admin.report.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 报告更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportUpdateReqVO extends ReportBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "21213")
    @NotNull(message = "id不能为空")
    private Long id;

}
