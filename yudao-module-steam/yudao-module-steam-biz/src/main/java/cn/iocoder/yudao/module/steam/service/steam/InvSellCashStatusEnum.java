package cn.iocoder.yudao.module.steam.service.steam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvSellCashStatusEnum {

    INIT(0, "未收款"),
    CASHED(1, "已收款"),
    DAMAGES(2, "已退款并收取违约金");
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;
}
