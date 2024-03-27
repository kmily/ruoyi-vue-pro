package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 悠悠商品模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class YouyouTemplateRespVO {

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

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    @ExcelProperty("图片地址")
    private String iconUrl;

    @Schema(description = "在售最低价", example = "17990")
    @ExcelProperty("在售最低价")
    private String minSellPrice;

    @Schema(description = "极速发货在售最低价", example = "12096")
    @ExcelProperty("极速发货在售最低价")
    private String fastShippingMinSellPrice;

    @Schema(description = "模板参考价", example = "25408")
    @ExcelProperty("模板参考价")
    private String referencePrice;

    @Schema(description = "外观", example = "赵六")
    @ExcelProperty("外观")
    private String exteriorName;

    @Schema(description = "稀有度", example = "张三")
    @ExcelProperty("稀有度")
    private String rarityName;

    @Schema(description = "品质", example = "李四")
    @ExcelProperty("品质")
    private String qualityName;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28997")
    @ExcelProperty("模板ID")
    private Integer templateId;

    @Schema(description = "在售数量")
    @ExcelProperty("在售数量")
    private String sellNum;

}