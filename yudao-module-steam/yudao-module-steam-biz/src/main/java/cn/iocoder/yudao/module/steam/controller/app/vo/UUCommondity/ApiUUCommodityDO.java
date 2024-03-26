package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;

import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;


/**
 * UU查询商品列表响应结果集
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiUUCommodityDO extends ApiResult {

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

        @Schema(description = "印花数据")
        @ExcelProperty("印花数据")
        private List<CommodityStickersDTO> commodityStickers;

//        @Schema(description = "多普勒属性")
//        @ExcelProperty("多普勒属性")
//        private List<CommodityDopplerDTO> commodityDoppler;

//        @Schema(description = "渐变色属性")
//        @ExcelProperty("渐变色属性")
//        private List<CommodityFadeDTO> commodityFade;

//        @Schema(description = "表面淬火属性")
//        @ExcelProperty("表面淬火属性")
//        private List<CommodityHardenedDTO> commodityHardened;

        /**
         * 印花数据
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class CommodityStickersDTO {
            @Schema(description = "印花Id")
            @ExcelProperty("印花Id")
            private Integer commodityPaintIndex;

            @Schema(description = "插槽编号")
            @ExcelProperty("插槽编号")
            private Integer rawIndex;

            @Schema(description = "印花名称")
            @ExcelProperty("印花名称")
            private String name;

            @Schema(description = "唯一名称")
            @ExcelProperty("唯一名称")
            private String hashName;

            @Schema(description = "材料")
            @ExcelProperty("材料")
            private String material;

            @Schema(description = "图片链接地址")
            @ExcelProperty("图片链接地址")
            private String imgUrl;

            @Schema(description = "印花价格(单位元)")
            @ExcelProperty("印花价格(单位元)")
            private String  price;

            @Schema(description = "印花价格(单位元)")
            @ExcelProperty("印花价格(单位元)")
            private String abrade;

        }

        /**
         * 多普勒属性
         */
//        @Data
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        private static class CommodityDopplerDTO {
//            @Schema(description = "分类名称")
//            @ExcelProperty("分类名称")
//            private String title;
//            @Schema(description = "分类缩写")
//            @ExcelProperty("分类缩写")
//            private String abbrTitle;
//            @Schema(description = "显示颜色")
//            @ExcelProperty("显示颜色")
//            private String color;
//        }

        /**
         * 表面淬火属性
         */
//        @Data
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        private static class CommodityHardenedDTO{
//            @Schema(description = "分类名称")
//            @ExcelProperty("分类名称")
//            private String title;
//            @Schema(description = "分类缩写")
//            @ExcelProperty("分类缩写")
//            private String abbrTitle;
//            @Schema(description = "显示颜色")
//            @ExcelProperty("显示颜色")
//            private String color;
//        }

        /**
         * 渐变色属性
         */
//        @Data
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        private static class CommodityFadeDTO {
//
//            @Schema(description = "分类名称")
//            @ExcelProperty("分类名称")
//            private String title;
//
//            @Schema(description = "分类缩写")
//            @ExcelProperty("分类缩写")
//            private String abbrTitle;
//
//            @Schema(description = "显示颜色")
//            @ExcelProperty("显示颜色")
//            private String color;
//        }

}
