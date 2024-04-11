package cn.iocoder.yudao.module.steam.service.fin.vo;

import lombok.Data;

import java.util.List;


@Data
public class ProductPriceInfoRes {

    // 定义返回数据的结构类
    @Data
    public static class ProductPriceInfoResponse {
        private boolean success;
        private List<ProductData> data;
        private int errorCode;
        private String errorMsg;
        private Object errorData;

        // 内部类，表示产品数据
        @Data
        public static class ProductData {
            private int appId;
            private String itemId;
            private String marketHashName;
            private double price;
            private int quantity;
            private double autoDeliverPrice;
            private int autoDeliverQuantity;
            private double manualDeliverPrice;
            private int manualQuantity;
        }
    }
}