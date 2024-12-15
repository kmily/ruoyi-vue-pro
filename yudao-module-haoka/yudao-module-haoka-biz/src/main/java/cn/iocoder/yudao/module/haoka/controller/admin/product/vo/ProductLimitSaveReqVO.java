package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;

@Schema(description = "管理后台 - 产品限制条件新增/修改 Request VO")
@Data
public class ProductLimitSaveReqVO {

    @Schema(description = "产品类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24464")
    private Long id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "产品类型名称不能为空")
    private String name;

    @Schema(description = "是否使用只发货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用只发货地址不能为空")
    private Boolean useOnlySendArea;

    @Schema(description = "是否使用不发货地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用不发货地址不能为空")
    private Boolean useNotSendArea;

    @Schema(description = "是否使用身份证限制", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用身份证限制不能为空")
    private Boolean useCardLimit;

    @Schema(description = "最大年龄限制")
    private Integer ageMax;

    @Schema(description = "最小年龄限制")
    private Integer ageMin;

    @Schema(description = "单人开卡数量限制")
    private Integer personCardQuantityLimit;

    @Schema(description = "检测周期(月)")
    private Integer detectionCycle;

}