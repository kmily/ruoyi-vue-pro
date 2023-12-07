package cn.iocoder.yudao.module.trade.controller.app.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 App - 交易订单创建 Request VO")
@Data
public class AppTradeOrderCreateReqVO extends AppTradeOrderSettlementReqVO {

    @Schema(description = "备注", example = "这个是我的订单哟")
    private String remark;

    @Schema(description = "服务日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
    @NotNull(message = "服务日期不能为空")
    private String serviceDate;

    @Schema(description = "服务时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "10:00-12:00")
    @NotNull(message = "服务时间不能为空")
    private String serviceTime;

    @Schema(description = "被护人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
    @NotNull(message = "被护人不能为空")
    private Long servicePersonId;

    @Schema(description = "图片资料", example = "888")
    private List<String> images;

    @Schema(description = "指派类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "USER")
    @NotBlank(message = "指派类型不能为空")
    @Pattern(message = "指派类型只能是，USER 或 SYSTEM", regexp = "USER|SYSTEM")
    private String assignType;


    @AssertTrue(message = "配送方式不能为空")
    @JsonIgnore
    public boolean isDeliveryTypeNotNull() {
        return getDeliveryType() != null;
    }

}
