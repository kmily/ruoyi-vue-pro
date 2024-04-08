package cn.iocoder.yudao.module.steam.enums;


public enum SteamQualityEnum {
    normal("normal","普通"," "),
    strange("strange","StatTrak™","#cf6a32"),
    tournament("tournament","纪念品","#ffb100"),
    unusual("unusual","★","#8650ac"),
    unusual_strange("unusual_strange","★ StatTrak™","#8650ac");
    private String code;
    private String name;
    private String color;

    SteamQualityEnum(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }
}
