package cn.iocoder.yudao.module.radar.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 雷达模块banner图 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BannerRespVO extends BannerBaseVO {

    @Schema(description = "自增编号", required = true, example = "12975")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
