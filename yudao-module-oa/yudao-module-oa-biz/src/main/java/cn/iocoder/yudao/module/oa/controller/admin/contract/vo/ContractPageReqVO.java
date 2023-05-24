package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 合同分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPageReqVO extends PageParam {

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "合同状态", example = "1")
    private Boolean status;

    @Schema(description = "审批状态", example = "1")
    private Boolean approvalStatus;

    @Schema(description = "创建者")
    private String creator;

}
