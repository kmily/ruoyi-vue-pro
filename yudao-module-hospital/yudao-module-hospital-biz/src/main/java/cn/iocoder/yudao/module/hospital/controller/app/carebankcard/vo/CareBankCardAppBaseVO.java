package cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 提现银行卡 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CareBankCardAppBaseVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "26294")
    private Long careId;

    @Schema(description = "持卡人", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotNull(message = "持卡人不能为空")
    private String name;

    @Schema(description = "卡号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "卡号不能为空")
    private String cardNo;

    @Schema(description = "开户行", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开户行不能为空")
    private String bank;

    @Schema(description = "身份证", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "身份证不能为空")
    private String idCard;

    @Schema(description = "预留手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预留手机号不能为空")
    private String mobile;

    @Schema(description = "是否默认", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否默认不能为空")
    private Boolean defaultStatus;

}
