package cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.*;
@Schema(description = "管理后台 - 外观选择新增/修改 Request VO")
@Data
public class SelExteriorSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "000001")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "WearCategory1")
    @NotEmpty(message = "名字不能为空")
    private String internalName;

    @Schema(description = "中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "略有磨损")
    @NotEmpty(message = "中文名称不能为空")
    private String localizedTagName;

}