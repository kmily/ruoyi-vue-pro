package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 悠悠商品模板新增/修改 Request VO")
@Data
public class UUTemplateSaveReqVO {

    @Schema(description = "武器全称", example = "印花 | Hello 法玛斯")
    private String name;

    @Schema(description = "武器英文全称", example = "Sticker | Hello FAMAS")
    private String hashName;

    @Schema(description = "类型编号", example = "106")
    private Integer typeId;

    @Schema(description = "类型名称", example = "印花")
    private String typeName;

    @Schema(description = "类型英文名称", example = "CSGO_Tool_Sticker")
    private String typeHashName;

    @Schema(description = "武器编号", example = "452")
    private Integer weaponId;

    @Schema(description = "武器名称", example = "压枪")
    private String weaponName;

    @Schema(description = "武器英文名称", example = "crate_sticker_pack_recoil_lootlist")
    private String weaponHashName;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11138")
    private Integer id;

    @Schema(description = "模板ID", example = "1110")
    private Integer templateId;

}