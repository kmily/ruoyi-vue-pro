package cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 悠悠商品列表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UUCommodityRespVO implements Serializable {

    @Schema(description = "商品id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13196")
    @ExcelProperty("商品id")
    private Integer id;

    @Schema(description = "商品模板id", example = "15581")
    @ExcelProperty("商品模板id")
    private Integer templateId;

    @Schema(description = "商品名称", example = "王五")
    @ExcelProperty("商品名称")
    private String commodityName;

    @Schema(description = "商品价格（单位元）", example = "13598")
    @ExcelProperty("商品价格（单位元）")
    private String commodityPrice;

    @Schema(description = "商品磨损度")
    @ExcelProperty("商品磨损度")
    private String commodityAbrade;

    @Schema(description = "图案模板")
    @ExcelProperty("图案模板")
    private Integer commodityPaintSeed;

    @Schema(description = "皮肤编号")
    @ExcelProperty("皮肤编号")
    private Integer commodityPaintIndex;

    @Schema(description = "是否有名称标签：0否1是")
    @ExcelProperty("是否有名称标签：0否1是")
    private Integer commodityHaveNameTag;

    @Schema(description = "是否有布章：0否1是")
    @ExcelProperty("是否有布章：0否1是")
    private Integer commodityHaveBuzhang;

    @Schema(description = "是否有印花：0否1是")
    @ExcelProperty("是否有印花：0否1是")
    private Integer commodityHaveSticker;

    @Schema(description = "发货模式：0,卖家直发；1,极速发货")
    @ExcelProperty("发货模式：0,卖家直发；1,极速发货")
    private Integer shippingMode;

    @Schema(description = "是否渐变色：0否1是")
    @ExcelProperty("是否渐变色：0否1是")
    private Integer templateisFade;

    @Schema(description = "Integer	是否表面淬火：0否1是")
    @ExcelProperty("Integer	是否表面淬火：0否1是")
    private Integer templateisHardened;

    @Schema(description = "是否多普勒：0否1是")
    @ExcelProperty("是否多普勒：0否1是")
    private Integer templateisDoppler;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "印花")
    @ExcelProperty("印花")
    private String commodityStickers;

    @Schema(description = "多普勒属性")
    @ExcelProperty("多普勒属性")
    private String commodityDoppler;

    @Schema(description = "渐变色属性")
    @ExcelProperty("渐变色属性")
    private String commodityFade;

    @Schema(description = "表面淬火属性")
    @ExcelProperty("表面淬火属性")
    private String commodityHardened;

    @Schema(description = "发货状态", example = "2")
    @ExcelProperty("发货状态")
    private Integer transferStatus;

    @Schema(description = "悠悠商品是否有效0开启1关闭", example = "1")
    @ExcelProperty("悠悠商品是否有效0开启1关闭")
    private Integer status;

}