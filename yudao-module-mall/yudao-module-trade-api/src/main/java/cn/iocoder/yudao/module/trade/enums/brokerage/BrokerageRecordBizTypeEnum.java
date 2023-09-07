package cn.iocoder.yudao.module.trade.enums.brokerage;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 佣金记录业务类型枚举
 *
 * @author owen
 */
@AllArgsConstructor
@Getter
public enum BrokerageRecordBizTypeEnum implements IntArrayValuable {

    // TODO @疯狂：这个也从 1 开始哇
    ORDER(0, "获得推广佣金", "获得推广佣金 {}", true),
    WITHDRAW(1, "提现申请", "提现申请扣除佣金 {}", false),
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(BrokerageRecordBizTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 标题
     */
    private final String title;
    /**
     * 描述
     */
    private final String description;
    /**
     * 是否为增加佣金
     */
    private final boolean add;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
