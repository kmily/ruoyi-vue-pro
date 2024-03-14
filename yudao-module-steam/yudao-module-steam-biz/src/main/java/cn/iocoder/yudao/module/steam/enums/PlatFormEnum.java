package cn.iocoder.yudao.module.steam.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PlatFormEnum  implements IntArrayValuable {
    WEB(1,"网页"),
    API(20,"接口");
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PlatFormEnum::getCode).toArray();

    private Integer code;
    private String name;
    public static PlatFormEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getCode().equals(value), PlatFormEnum.values());
    }


    @Override
    public int[] array() {
        return ARRAYS;
    }
}
