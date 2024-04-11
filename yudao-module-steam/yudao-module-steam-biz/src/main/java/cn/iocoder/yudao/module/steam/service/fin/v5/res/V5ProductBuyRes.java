package cn.iocoder.yudao.module.steam.service.fin.v5.res;


import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
@Data
public class V5ProductBuyRes implements Serializable {
    private int code;
    private String msg;
    private PaymentData data;
    @Data
    public static class PaymentData {
        private int payAmount;
        private ReceiverInfo receiverInfo;
        private String merchantOrderNo;
        private String orderNo;

        @Data
        public static class ReceiverInfo {

            private String headPicture;
            private String steamCreateDate;
            private String nickname;

        }
    }


}