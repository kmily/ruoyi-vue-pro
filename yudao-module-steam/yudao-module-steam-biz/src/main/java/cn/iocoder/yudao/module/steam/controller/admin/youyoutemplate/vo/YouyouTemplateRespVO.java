package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 悠悠商品模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class YouyouTemplateRespVO implements Serializable {

    @Schema(description = "武器全称", example = "印花 | Hello 法玛斯")
    @ExcelProperty("武器全称")
    private String name;

    @Schema(description = "武器英文全称", example = "Sticker | Hello FAMAS")
    @ExcelProperty("武器英文全称")
    private String hashName;

    @Schema(description = "类型编号", example = "106")
    @ExcelProperty("类型编号")
    private Integer typeId;

    @Schema(description = "类型名称", example = "印花")
    @ExcelProperty("类型名称")
    private String typeName;

    @Schema(description = "类型英文名称", example = "CSGO_Tool_Sticker")
    @ExcelProperty("类型英文名称")
    private String typeHashName;

    @Schema(description = "武器编号", example = "452")
    @ExcelProperty("武器编号")
    private Integer weaponId;

    @Schema(description = "武器名称", example = "压枪")
    @ExcelProperty("武器名称")
    private String weaponName;

    @Schema(description = "武器英文名称", example = "crate_sticker_pack_recoil_lootlist")
    @ExcelProperty("武器英文名称")
    private String weaponHashName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11138")
    @ExcelProperty("主键ID")
    private Integer id;

    @Schema(description = "模板ID", example = "1110")
    @ExcelProperty("模板ID")
    private Integer templateId;

}