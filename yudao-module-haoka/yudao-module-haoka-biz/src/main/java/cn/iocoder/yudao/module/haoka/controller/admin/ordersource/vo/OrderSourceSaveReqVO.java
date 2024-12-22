package cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceLiveDO;

@Schema(description = "管理后台 - 订单来源配置新增/修改 Request VO")
@Data
public class OrderSourceSaveReqVO {

    @Schema(description = "来源ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25277")
    private Long id;

    @Schema(description = "来源备注", example = "你猜")
    private String sourceRemark;

    @Schema(description = "渠道ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "渠道ID不能为空")
    private Long channel;

}