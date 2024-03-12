package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.pay.core.enums.transfer.PayTransferTypeEnum;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class WithdrawalInfo implements Serializable {
    /**
     * 收款人姓名
     */
    @NotBlank(message = "收款人姓名不能为空", groups = {PayTransferTypeEnum.Alipay.class})
    private String userName;
    /**
     * 支付宝登录号
     */
    @NotBlank(message = "支付宝登录号不能为空", groups = {PayTransferTypeEnum.Alipay.class})
    private String alipayLogonId;
}
