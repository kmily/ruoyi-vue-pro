package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - steam订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvOrderExtReqVo {
    @Schema(description = "业务订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "业务订单号不能为空")
    private Long id;
}
