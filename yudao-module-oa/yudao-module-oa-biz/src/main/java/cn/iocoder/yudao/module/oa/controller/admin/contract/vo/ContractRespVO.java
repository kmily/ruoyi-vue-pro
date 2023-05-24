package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractRespVO extends ContractBaseVO {

    @Schema(description = "合同编号", required = true)
    private String contractNo;

    @Schema(description = "审批状态", example = "1")
    private Boolean approvalStatus;

    @Schema(description = "创建者")
    private String creator;

}
