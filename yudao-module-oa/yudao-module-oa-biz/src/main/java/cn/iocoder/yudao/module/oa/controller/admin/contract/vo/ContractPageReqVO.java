package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 合同分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPageReqVO extends PageParam {

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "合同状态", example = "1")
    private int status;

    @Schema(description = "审批状态", example = "1")
    private int approvalStatus;

    @Schema(description = "创建者")
    private String creator;

}
