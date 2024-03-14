package cn.iocoder.yudao.module.pay.enums.wallet;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 钱包交易业务分类
 *
 * @author jason
 */
@AllArgsConstructor
@Getter
public enum PayWalletBizTypeEnum implements IntArrayValuable {

    RECHARGE(1, "充值"),
    RECHARGE_REFUND(2, "充值退款"),
    PAYMENT(3, "支付"),
    PAYMENT_REFUND(4, "支付退款"),
    STEAM_PAY(100,"steam交易支付"),
    STEAM_CASH(101,"steam交易收款"),
    SERVICE_FEE(102,"提现手续费"),
    INV_SERVICE_FEE(103,"购买服务手续费");

    // TODO 后续增加

    /**
     * 业务分类
     */
    private final Integer type;
    /**
     * 说明
     */
    private final String description;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PayWalletBizTypeEnum::getType).toArray();

    @Override
    public int[] array() {
         return ARRAYS;
    }
}
