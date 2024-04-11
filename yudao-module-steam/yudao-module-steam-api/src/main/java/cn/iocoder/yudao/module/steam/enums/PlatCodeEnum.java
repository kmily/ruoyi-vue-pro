package cn.iocoder.yudao.module.steam.enums;

public enum PlatCodeEnum {
    UNSUPPORTED("UNSUPPORTED","unsupported"),
    C5("C5","C5"),
    V5("V5","V5"),
    IG("IG","IG");

    PlatCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;
}
