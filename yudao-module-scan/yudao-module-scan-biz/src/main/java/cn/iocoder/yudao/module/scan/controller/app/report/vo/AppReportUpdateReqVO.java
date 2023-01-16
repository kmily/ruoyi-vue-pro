package cn.iocoder.yudao.module.scan.controller.app.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("扫描APP - 报告更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppReportUpdateReqVO extends AppReportBaseVO {

    @ApiModelProperty(value = "id", required = true, example = "21213")
    @NotNull(message = "id不能为空")
    private Long id;

}
