package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;
import lombok.Data;

import java.util.List;

@Data
public class AppOtherInvListDto {

    private boolean success;

    private Data_ data;

    @Data
    public static class Data_ {

        // 外观
        private Integer total;

        // 外观名称
        private Integer pages;

        // 图片
        private Integer page;

        // 图片
        private Integer limit;

        // 价格和在售数量信息
        private List<GoodsInfo> list;

        @Data
        public static class GoodsInfo {
            // 自动发货在售最低价
            private Integer appId;

            // 自动发货在售数量
            private String itemId;

            private PriceInfo priceInfo;

            @Data
            public static class PriceInfo{
                private String userId;
                private Float price;
                private Integer quantity;
                private Float autoDeliverPrice;
                private Integer autoDeliverQuantity;
                private Float manualDeliverPrice;
                private Integer manualQuantity;
            }
            // 自动发货在售最低价
            private String itemName;

            // 自动发货在售数量
            private String marketHashName;

            // 品质
            private String shortName;

            // 品质颜色
            private String imageUrl;

            // 品质名称
            private String type;

            // 短名称，去掉前缀
            private String typeName;

            // 类型
            private String quality;

            // 类型名称
            private String qualityName;

            // 自动发货在售最低价
            private String qualityColor;

            // 自动发货在售数量
            private String rarity;

            // 品质
            private String rarityName;

            // 品质颜色
            private String rarityColor;

            // 品质名称
            private String exterior;

            // 短名称，去掉前缀
            private String exteriorName;
        }
    }

    private Integer errorCode;
    private String  errorMsg;
    private String  errorData;
}
