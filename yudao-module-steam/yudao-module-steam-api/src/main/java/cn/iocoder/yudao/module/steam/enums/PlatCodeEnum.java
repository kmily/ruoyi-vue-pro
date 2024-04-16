package cn.iocoder.yudao.module.steam.enums;

public enum PlatCodeEnum {
    UNSUPPORTED("UNSUPPORTED","unsupported"),
    IO661("IO661","IO661"),
    C5("C5","C5"),
    V5("V5","V5"),
    IG("IG","IG");

    PlatCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
