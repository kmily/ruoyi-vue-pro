package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class C5ItemInfo {

    @JsonProperty("quality")
    private String quality;
    @JsonProperty("qualityName")
    private String qualityName;
    @JsonProperty("qualityColor")
    private String qualityColor;
    @JsonProperty("rarity")
    private String rarity;
    @JsonProperty("rarityName")
    private String rarityName;
    @JsonProperty("rarityColor")
    private String rarityColor;
    @JsonProperty("type")
    private String type;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("slot")
    private String slot;
    @JsonProperty("slotName")
    private String slotName;
    @JsonProperty("hero")
    private String hero;
    @JsonProperty("heroName")
    private String heroName;
    @JsonProperty("heroAvatar")
    private String heroAvatar;
    @JsonProperty("exterior")
    private String exterior;
    @JsonProperty("exteriorName")
    private String exteriorName;
    @JsonProperty("exteriorColor")
    private String exteriorColor;
    @JsonProperty("weapon")
    private String weapon;
    @JsonProperty("weaponName")
    private String weaponName;
    @JsonProperty("itemSet")
    private String itemSet;
    @JsonProperty("itemSetName")
    private String itemSetName;
    @JsonProperty("stickerCapsule")
    private String stickerCapsule;
    @JsonProperty("stickerCapsuleName")
    private String stickerCapsuleName;
    @JsonProperty("patchCapsule")
    private String patchCapsule;
    @JsonProperty("patchCapsuleName")
    private String patchCapsuleName;
    @JsonProperty("customPlayer")
    private String customPlayer;
    @JsonProperty("customPlayerName")
    private String customPlayerName;
    @JsonProperty("category")
    private String category;
    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("item")
    private String item;
    @JsonProperty("itemName")
    private String itemName;
}
