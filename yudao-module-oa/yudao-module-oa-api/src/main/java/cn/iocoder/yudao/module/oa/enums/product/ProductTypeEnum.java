package cn.iocoder.yudao.module.oa.enums.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductTypeEnum {

    MACHINE(0, "整机"),
    APPENDIX(1, "附件"),
    SOFTWARE(2, "软件服务");

    /**
     * 类型编码
     */
    private final int code;
    /**
     * 类型名称
     */
    private final String name;
}
