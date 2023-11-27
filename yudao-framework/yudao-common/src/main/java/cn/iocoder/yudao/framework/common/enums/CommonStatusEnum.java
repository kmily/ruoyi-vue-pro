package cn.iocoder.yudao.framework.common.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IntArrayValuable {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭"),

    YES(1, "是"),
    NO(0, "否"),

    /**
     * 开启中
     */
    OPEN(1,"开启中"),
    /**
     * 店铺关闭
     */
    CLOSED(2,"关闭"),
    /**
     * 申请开店
     */
    APPLY(3,"申请"),
    /**
     * 审核拒绝
     */
    REFUSED(4,"审核拒绝"),
    /**
     * 申请中
     */
    APPLYING(5,"待审核");;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CommonStatusEnum::getStatus).toArray();

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
