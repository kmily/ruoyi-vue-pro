package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 睡眠统计记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HealthStatisticsRespVO extends HealthStatisticsBaseVO implements Comparable<HealthStatisticsRespVO> {

    @Schema(description = "自增编号", required = true, example = "17097")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

    @Override
    public int compareTo(@NotNull HealthStatisticsRespVO o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
