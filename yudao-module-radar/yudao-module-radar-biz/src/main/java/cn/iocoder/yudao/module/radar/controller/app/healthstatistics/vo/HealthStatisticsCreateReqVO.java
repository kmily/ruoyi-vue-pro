package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 睡眠统计记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthStatisticsCreateReqVO extends HealthStatisticsBaseVO {

}
