package cn.iocoder.yudao.module.steam.controller.app.ad.vo;//package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class AdBlockReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22314")
    private Long blockId;

}
