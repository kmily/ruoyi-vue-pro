package cn.iocoder.yudao.module.steam.enums;


public enum SteamWearCategoryEnum {
    WearCategoryNA("WearCategoryNA","无涂装"," "),
    WearCategory0("WearCategory0","崭新出厂","#228149"),
    WearCategory1("WearCategory1","略有磨损","#64b02b"),
    WearCategory2("WearCategory2","久经沙场","#efad4d"),
    WearCategory3("WearCategory3","破损不堪","#b7625f"),
    WearCategory4("WearCategory4","战痕累累","#993a38");
    private String code;
    private String name;
    private String color;

    SteamWearCategoryEnum(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }
}
