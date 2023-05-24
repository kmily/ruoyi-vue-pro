package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 合同创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractCreateReqVO extends ContractBaseVO {

    @Schema(description = "合同编号", required = true)
    @NotNull(message = "合同编号不能为空")
    private String contractNo;

    @Schema(description = "审批状态", example = "1")
    private Boolean approvalStatus;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "更新者")
    private String updater;

}
