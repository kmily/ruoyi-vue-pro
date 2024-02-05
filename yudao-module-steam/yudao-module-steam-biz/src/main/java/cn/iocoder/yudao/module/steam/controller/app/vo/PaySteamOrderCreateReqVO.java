package cn.iocoder.yudao.module.steam.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 示例订单创建 Request VO")
@Data
public class PaySteamOrderCreateReqVO {


    @Schema(description = "价格不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "assetId不能为空")
    private String assetId;
    @Schema(description = "价格不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "classId不能为空")
    private String classId;
    @Schema(description = "价格不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "实例ID不能为空")
    private String instanceId;


    @Schema(description = "价格不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "价格不能为空")
    private Integer price;
    @Schema(description = "商品名称不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "商品名称不能为空")
    private String name;

}
