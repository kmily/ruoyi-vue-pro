package cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 资质信息 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AptitudeBaseVO {

    @Schema(description = "资质名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotNull(message = "资质名称不能为空")
    private String name;

    @Schema(description = "资质图标", example = "https://www.iocoder.cn")
    private String picUrl;

    @Schema(description = "状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态（0正常 1停用）不能为空")
    private Byte status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
