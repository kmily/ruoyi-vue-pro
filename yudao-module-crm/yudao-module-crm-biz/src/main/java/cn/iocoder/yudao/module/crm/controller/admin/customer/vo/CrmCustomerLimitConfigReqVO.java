package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - CRM 客户限制配置 Request VO")
@Data
public class CrmCustomerLimitConfigReqVO {

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "规则类型 1: 拥有客户数限制，2:锁定客户数限制", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer type;

    @Schema(description = "规则适用人群", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String userIds;

    @Schema(description = "规则适用部门", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private String deptIds;

    @Schema(description = "成交客户是否占有拥有客户数(当 type = 1 时)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer dealCountEnabled;

}
