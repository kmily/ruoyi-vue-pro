package cn.iocoder.yudao.module.statistics.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品概况 环比数据
 * @Author：麦子
 */
@Schema(description = "管理后台 - 商品统计 Response VO")
@Data
public class ProductStatisticsComparisonRespVO {

    @Schema(description = "新增商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer newProductNum;

    @Schema(description = "新增商品数量环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer newProductNumRatio;

    @Schema(description = "浏览量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1023")
    private Integer pageView;

    @Schema(description = "浏览量环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1022")
    private Integer pageViewRatio;

    @Schema(description = "收藏量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer collectNum;

    @Schema(description = "收藏量环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer collectNumRatio;

    @Schema(description = "加购件数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer addCartNum;

    @Schema(description = "加购件数环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer addCartNumRatio;

    @Schema(description = "交易总件数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer orderProductNum;

    @Schema(description = "交易总件数环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer orderProductNumRatio;

    @Schema(description = "交易成功件数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer orderSuccessProductNum;

    @Schema(description = "交易成功件数环比", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer orderSuccessProductNumRatio;

}
