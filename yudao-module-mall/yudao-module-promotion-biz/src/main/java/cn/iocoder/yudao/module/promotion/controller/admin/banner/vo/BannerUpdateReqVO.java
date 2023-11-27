package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - Banner更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BannerUpdateReqVO extends BannerBaseVO {

    @Schema(description = "Banner 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25208")
    @NotNull(message = "Banner 编号不能为空")
    private Long id;

    @Schema(description = "Banner 点击次数", example = "16196")
    private Integer browseCount;

}
