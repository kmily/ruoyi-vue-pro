package cn.iocoder.yudao.module.steam.controller.app.vo.UUBatchGetOnSaleCommodity;

import lombok.Data;

@Data
public class UUSaleTemplateRespVO {

    private SaleTemplateResponse saleTemplateResponse;

    private SaleCommodityResponse saleCommodityResponse;

    @Data
    public static class SaleTemplateResponse {

        // 商品模板id
        private String templateId;

        // 模板hashName
        private String templateHashName;

        // 模板图片链接
        private String iconUrl;

        // 外观名称
        private String exteriorName;

        // 品质
        private String rarityName;

        // 类别
        private String qualityName;
    }

    @Data
    public static class SaleCommodityResponse{

        // 在售最低价(单位：元)
        private String minSellPrice;

        // 极速发货在售最低价(单位：元)
        private String fastShippingMinSellPrice;

        // 模板参考价(单位：元)
        private String referencePrice;

        // 在售数量
        private String sellNum;

    }

}
