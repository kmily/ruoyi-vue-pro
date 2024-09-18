package cn.iocoder.yudao.module.promotion.enums.reward;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import cn.iocoder.yudao.module.promotion.enums.diy.DiyPageEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 满减类型枚举
 *
 * @author jason
 */
@AllArgsConstructor
@Getter
public enum RewardTypeEnum implements IntArrayValuable {

    FULLMINUS(1, "满减"),
    FULLDELIVER(2, "满送"),
    FREEDELIVERY(3, "包邮"),
    FULLDELIVERSUM(4, "满送券合计"),
    ;

    private static final int[] ARRAYS = Arrays.stream(values()).mapToInt(RewardTypeEnum::getType).toArray();

    /**
     * 页面编号
     */
    private final Integer type;

    /**
     * 页面名称
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
