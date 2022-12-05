package cn.iocoder.yudao.module.pay.controller.admin.merchant.vo.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(title = "管理后台 - 支付商户信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayMerchantCreateReqVO extends PayMerchantBaseVO {

}
