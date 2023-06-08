package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* 合同 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ContractBaseVO {

    @Schema(description = "公司类型", example = "1")
    private String companyType;

    @Schema(description = "客户id", required = true, example = "22524")
    @NotNull(message = "客户id不能为空")
    private Long customerId;

    @Schema(description = "供方代表", required = true, example = "17123")
    @NotNull(message = "供方代表不能为空")
    private Long supplierUserId;

    @Schema(description = "总款")
    private BigDecimal totalFee;

    @Schema(description = "劳务费")
    private BigDecimal serviceFee;

    @Schema(description = "佣金")
    private BigDecimal commissions;

    @Schema(description = "零星费用")
    private BigDecimal otherFee;

    @Schema(description = "工程实施联系人", example = "张三")
    private String implContactName;

    @Schema(description = "工程实施联系电话")
    private String implContactPhone;

    @Schema(description = "合同状态", example = "1")
    private int status;

}
