package cn.iocoder.yudao.module.steam.controller.app.vo.UUSellingList;

import lombok.Data;

import java.util.List;
@Data
public class QueryUUSellingListReqVO {

    // 当前页
    private Integer currentPage;

    // 下一页是否还有内容
    private Boolean newPageIsHaveContent;

    private List<SaleTemplateByCategoryResponseList> saleTemplateByCategoryResponseList;

    @Data
    private static class SaleTemplateByCategoryResponseList {


        // 模板ID
        private Integer templateId;


        // 模板哈希名称
        private String templateHashName;

        // 模板名称
        private String templateName;

        // 图标URL
        private String iconUrl;

        // 外观名称
        private String exteriorName;

        // 稀有度名称
        private String rarityName;

        // 类型ID
        private Integer typeId;

        // 类型哈希名称
        private String typeHashName;

        // 武器ID
        private Integer weaponId;

        // 武器哈希名称
        private String weaponHashName;

        // 最小销售价格
        private String minSellPrice;

        // 快速销售最小销售价格
        private String fastShippingMinSellPrice;

        // 参考价格
        private String referencePrice;

        // 销售数量
        private Integer sellNum;

    }

}
