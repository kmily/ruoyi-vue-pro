package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author xia
 */
@Schema(title = "管理后台 - Banner Response VO")
@Data
@ToString(callSuper = true)
public class BannerRespVO  extends BannerBaseVO {

    @Schema(title = "banner编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "banner编号不能为空")
    private Long id;

    @Schema(title = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
