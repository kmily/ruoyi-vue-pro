package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 医护收藏 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AppCareFavoriteBaseVO {

    @Schema(description = "会员编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9431")
    @NotNull(message = "会员编号不能为空")
    private Long memberId;

    @Schema(description = "医护编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16329")
    @NotNull(message = "医护编号不能为空")
    private Long careId;

}
