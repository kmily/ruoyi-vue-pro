package cn.iocoder.yudao.module.steam.controller.app.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户下拉选择接口
 * AppDevApiController
 * 2024-02-19 16:11 lgm
 */
public class DropListVO {
    // 匕首
    @JsonProperty("knife")
    private String CSGO_Type_Knife;

    // 步枪
    @JsonProperty("rifle")
    private String CSGO_Type_Rifle;

    // 手枪
    @JsonProperty("pistol")
    private String CSGO_Type_Pistol;

    // 手套
    @JsonProperty("glove")
    private String CSGO_Type_Hands;

    // 微型冲锋枪
    @JsonProperty("smg")
    private String CSGO_Type_SMG;

    // 霰弹枪
    @JsonProperty("shotgun")
    private String CSGO_Type_Shotgun;

    // 机枪
    @JsonProperty("machinegun")
    private String CSGO_Type_Machinegun;

    // 印花
    @JsonProperty("sticker")
    private String CSGO_Tool_Sticker;

    // 其他
    @JsonProperty("other")
    private String CSGO_Type_other;

    // 探员
    @JsonProperty("customplayer")
    private String CSGO_Type_customplayer;

}


