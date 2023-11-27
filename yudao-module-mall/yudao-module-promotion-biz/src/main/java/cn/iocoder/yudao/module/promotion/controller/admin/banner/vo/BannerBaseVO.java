package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * Banner Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BannerBaseVO {

    @Schema(description = "Banner 标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Banner 标题不能为空")
    private String title;

    @Schema(description = "图片 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotNull(message = "图片 URL不能为空")
    private String picUrl;

    @Schema(description = "跳转地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotNull(message = "跳转地址不能为空")
    private String url;

    @Schema(description = "活动状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "活动状态不能为空")
    private Byte status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "位置不能为空")
    private Byte position;

    @Schema(description = "描述", example = "你猜")
    private String memo;

}
