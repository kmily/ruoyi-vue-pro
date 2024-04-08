package cn.iocoder.yudao.module.steam.enums;


public enum SteamRarityEnum {
    Rarity_Rare_Weapon("Rarity_Rare_Weapon","军规级","#4b69ff"),
    Rarity_Legendary_Weapon("Rarity_Legendary_Weapon","保密","#d32ce6"),
    Rarity_Ancient_Weapon("Rarity_Ancient_Weapon","隐秘","#f07373"),
    Rarity_Mythical_Weapon("Rarity_Mythical_Weapon","受限","#8847ff"),
    Rarity_Uncommon_Weapon("Rarity_Uncommon_Weapon","工业级","#5e98d9"),
    Rarity_Common_Weapon("Rarity_Common_Weapon","消费级","#90accc"),
    Rarity_Ancient("Rarity_Ancient","非凡","#eb4b4b"),
    Rarity_Mythical("Rarity_Mythical","卓越","#8847ff"),
    Rarity_Legendary("Rarity_Legendary","奇异","#d32ce6"),
    Rarity_Rare("Rarity_Rare","高级","#4b69ff"),
    Rarity_Common("Rarity_Common","普通级","#90ACCC"),
    Rarity_Contraband("Rarity_Contraband","违禁","#E4AE39"),
    Rarity_Ancient_Character("Rarity_Ancient_Character","探员：大师","#EB4B4B"),
    Rarity_Legendary_Character("Rarity_Legendary_Character","探员：非凡","#D32CE6"),
    Rarity_Mythical_Character("Rarity_Mythical_Character","探员：卓越","#8847FF"),
    Rarity_Rare_Character("Rarity_Rare_Character","探员：高级","#4B69FF")
    ;
    private String code;
    private String name;
    private String color;

    SteamRarityEnum(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
