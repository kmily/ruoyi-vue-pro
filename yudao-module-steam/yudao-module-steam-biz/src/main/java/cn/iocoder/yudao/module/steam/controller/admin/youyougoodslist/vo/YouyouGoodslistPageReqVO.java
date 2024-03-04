package cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 查询商品列分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YouyouGoodslistPageReqVO extends PageParam {

    @Schema(description = "商品模板id", example = "30011")
    private Integer templateId;

    @Schema(description = "商品名称", example = "赵六")
    private String commodityName;

    @Schema(description = "商品价格（单位元）", example = "32696")
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

    @Schema(description = "印花Id", example = "29965")
    private Integer commodityStickersStickerId;

    @Schema(description = "插槽编号")
    private Integer commodityStickersRawIndex;

    @Schema(description = "印花名称", example = "芋艿")
    private String commodityStickersName;

    @Schema(description = "唯一名称", example = "张三")
    private String commodityStickersHashName;

    @Schema(description = "材料")
    private String commodityStickersMaterial;

    @Schema(description = "图片链接地址", example = "https://www.iocoder.cn")
    private String commodityStickersImgUrl;

    @Schema(description = "印花价格(单位元)", example = "26667")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}