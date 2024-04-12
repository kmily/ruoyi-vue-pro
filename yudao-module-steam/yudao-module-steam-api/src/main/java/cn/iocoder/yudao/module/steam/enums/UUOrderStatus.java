package cn.iocoder.yudao.module.steam.enums;


import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 大状态bigStatus说明
 */
@AllArgsConstructor
@Getter
public enum UUOrderStatus implements IntArrayValuable {
    CODE100(100,"创建中。"),
    CODE120(120,"支付中。"),
    CODE140(140,"交货中。"),
    CODE360(360,"结算中。"),
    CODE340(340,"已完成。"),
    CODE280(280,"已取消。"),
    CODE0(0,"成功"),
    CODE2(2,"失败");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(UUOrderStatus::getCode).toArray();

    private Integer code;
    private String msg;

    public static UUOrderStatus valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getCode().equals(value), UUOrderStatus.values());
    }


    @Override
    public int[] array() {
        return ARRAYS;
    }
}
