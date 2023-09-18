package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 睡眠统计记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthStatisticsUpdateReqVO extends HealthStatisticsBaseVO {

    @Schema(description = "自增编号", required = true, example = "17097")
    @NotNull(message = "自增编号不能为空")
    private Long id;

}
