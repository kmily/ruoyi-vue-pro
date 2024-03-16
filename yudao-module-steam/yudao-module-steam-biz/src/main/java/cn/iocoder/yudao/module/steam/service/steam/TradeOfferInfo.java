package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * steam交易订单信息
 */
@NoArgsConstructor
@Data
public class TradeOfferInfo {

    @JsonProperty("response")
    private ResponseDTO response;

    @NoArgsConstructor
    @Data
    public static class ResponseDTO {
        @JsonProperty("offer")
        private OfferDTO offer;

        @NoArgsConstructor
        @Data
        public static class OfferDTO {
            @JsonProperty("tradeofferid")
            private String tradeofferid;
            @JsonProperty("accountid_other")
            private Integer accountidOther;
            @JsonProperty("message")
            private String message;
            @JsonProperty("expiration_time")
            private Integer expirationTime;
            /**
             * k_ETradeOfferStateInvalid	1	Invalid
             * k_ETradeOfferStateActive	2	This trade offer has been sent, neither party has acted on it yet.
             * k_ETradeOfferStateAccepted	3	The trade offer was accepted by the recipient and items were exchanged.
             * k_ETradeOfferStateCountered	4	The recipient made a counter offer
             * k_ETradeOfferStateExpired	5	The trade offer was not accepted before the expiration date
             * k_ETradeOfferStateCanceled	6	The sender cancelled the offer
             * k_ETradeOfferStateDeclined	7	The recipient declined the offer
             * k_ETradeOfferStateInvalidItems	8	Some of the items in the offer are no longer available (indicated by the missing flag in the output)
             * k_ETradeOfferStateCreatedNeedsConfirmation	9	The offer hasn't been sent yet and is awaiting email/mobile confirmation. The offer is only visible to the sender.
             * k_ETradeOfferStateCanceledBySecondFactor	10	Either party canceled the offer via email/mobile. The offer is visible to both parties, even if the sender canceled it before it was sent.
             * k_ETradeOfferStateInEscrow	11	The trade has been placed on hold. The items involved in the trade have all been removed from both parties' inventories and will be automatically delivered in the future.
             *
             * 1 无效的
             * 2 这份交易要约已经发出，双方都还没有采取行动。
             * 3 接受方接受了交易要约，并交换了物品。
             * 4 接受者还价
             * 5交易要约在到期日前未被接受
             * 6 发件人取消了报价
             * 7 收件人拒绝了报价
             * 8 报价中的某些项目不再可用（由输出中缺失的标志表示）
             * 9 报价尚未发送，正在等待电子邮件/手机确认。该优惠仅对发件人可见。
             * 10 任何一方都通过电子邮件/手机取消了优惠。该优惠对双方都是可见的，即使发件人在发送之前取消了它。
             * 11 交易已被搁置。交易中涉及的物品已全部从双方的库存中移除，并将在未来自动交付。
             *
             */
            @JsonProperty("trade_offer_state")
            private Integer tradeOfferState;
            @JsonProperty("items_to_give")
            private List<ItemsToGiveDTO> itemsToGive;
            @JsonProperty("is_our_offer")
            private Boolean isOurOffer;
            @JsonProperty("time_created")
            private Integer timeCreated;
            @JsonProperty("time_updated")
            private Integer timeUpdated;
            @JsonProperty("from_real_time_trade")
            private Boolean fromRealTimeTrade;
            @JsonProperty("escrow_end_date")
            private Integer escrowEndDate;
            @JsonProperty("confirmation_method")
            private Integer confirmationMethod;
            @JsonProperty("eresult")
            private Integer eresult;

            @NoArgsConstructor
            @Data
            public static class ItemsToGiveDTO {
                @JsonProperty("appid")
                private Integer appid;
                @JsonProperty("contextid")
                private String contextid;
                @JsonProperty("assetid")
                private String assetid;
                @JsonProperty("classid")
                private String classid;
                @JsonProperty("instanceid")
                private String instanceid;
                @JsonProperty("amount")
                private String amount;
                @JsonProperty("missing")
                private Boolean missing;
                @JsonProperty("est_usd")
                private String estUsd;
            }
        }
    }
}
