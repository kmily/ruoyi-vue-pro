package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 睡眠统计记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthStatisticsCreateReqVO extends HealthStatisticsBaseVO {

}
