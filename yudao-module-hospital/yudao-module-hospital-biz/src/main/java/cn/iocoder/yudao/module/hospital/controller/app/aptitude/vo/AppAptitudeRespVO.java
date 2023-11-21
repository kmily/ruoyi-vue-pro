package cn.iocoder.yudao.module.hospital.controller.app.aptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "APP - 资质信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppAptitudeRespVO {

    @Schema(description = "收藏编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21395")
    private Long id;

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
