package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 产品的渠道新增/修改 Request VO")
@Data
public class ProductChannelSaveReqVO {

    @Schema(description = "产品类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "252")
    private Long id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品类型名称不能为空")
    private String name;

}