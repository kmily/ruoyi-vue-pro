package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Banner Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BannerRespVO extends BannerBaseVO {

    @Schema(description = "Banner 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25208")
    private Long id;

    @Schema(description = "Banner 点击次数", example = "16196")
    private Integer browseCount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
