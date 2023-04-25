package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 合同更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractUpdateReqVO extends ContractBaseVO {

    @Schema(description = "id", required = true, example = "16083")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
