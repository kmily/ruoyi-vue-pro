package cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 悠悠商品列表新增/修改 Request VO")
@Data
public class YouyouCommoditySaveReqVO {

    @Schema(description = "商品id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13196")
    private Integer id;

    @Schema(description = "商品模板id", example = "15581")
    private Integer templateId;

    @Schema(description = "商品名称", example = "王五")
    private String commodityName;

    @Schema(description = "商品价格（单位元）", example = "13598")
    private String commodityPrice;

    @Schema(description = "商品磨损度")
    private String commodityAbrade;

    @Schema(description = "图案模板")
    private Integer commodityPaintSeed;

    @Schema(description = "皮肤编号")
    private Integer commodityPaintIndex;

    @Schema(description = "是否有名称标签：0否1是")
    private Integer commodityHaveNameTag;

    @Schema(description = "是否有布章：0否1是")
    private Integer commodityHaveBuzhang;

    @Schema(description = "是否有印花：0否1是")
    private Integer commodityHaveSticker;

    @Schema(description = "发货模式：0,卖家直发；1,极速发货")
    private Integer shippingMode;

    @Schema(description = "是否渐变色：0否1是")
    private Integer templateisFade;

    @Schema(description = "Integer	是否表面淬火：0否1是")
    private Integer templateisHardened;

    @Schema(description = "是否多普勒：0否1是")
    private Integer templateisDoppler;

    @Schema(description = "印花Id", example = "22554")
    private Integer commodityStickersStickerId;

    @Schema(description = "插槽编号")
    private Integer commodityStickersRawIndex;

    @Schema(description = "印花名称", example = "张三")
    private String commodityStickersName;

    @Schema(description = "唯一名称", example = "芋艿")
    private String commodityStickersHashName;

    @Schema(description = "材料")
    private String commodityStickersMaterial;

    @Schema(description = "图片链接地址", example = "https://www.iocoder.cn")
    private String commodityStickersImgUrl;

    @Schema(description = "印花价格(单位元)", example = "1452")
    private String commodityStickersPrice;

    @Schema(description = "磨损值")
    private String commodityStickersAbrade;

    @Schema(description = "多普勒属性分类名称")
    private String commodityDopplerTitle;

    @Schema(description = "多普勒属性分类缩写")
    private String commodityDopplerAbbrTitle;

    @Schema(description = "多普勒属性显示颜色")
    private String commodityDopplerColor;

    @Schema(description = "渐变色属性属性名称")
    private String commodityFadeTitle;

    @Schema(description = "渐变色属性对应数值")
    private String commodityFadeNumerialValue;

    @Schema(description = "渐变色属性显示颜色")
    private String commodityFadeColor;

    @Schema(description = "表面淬火属性分类名称")
    private String commodityHardenedTitle;

    @Schema(description = "表面淬火属性分类缩写")
    private String commodityHardenedAbbrTitle;

    @Schema(description = "表面淬火属性显示颜色")
    private String commodityHardenedColor;

    @Schema(description = "发货状态", example = "2")
    private String transferStatus;

}