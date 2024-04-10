package cn.iocoder.yudao.module.steam.service.steam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtherSellingStatusEnum {

    IG(1, "IG"),
    C5(2, "C5"),
    UU(3, "UU"),
    BUFF(4, "BUFF");
    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;
}
