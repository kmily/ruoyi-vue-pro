package cn.iocoder.yudao.framework.common.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfConfigTypeEnum {

    /**
     * 系统配置
     */
    SYSTEM(1),
    /**
     * 自定义配置
     */
    CUSTOM(2);

    private final Integer type;

}
