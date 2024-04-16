package cn.iocoder.yudao.module.steam.service.fin.c5.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class C5FastPayVo implements Serializable {
    /**
     * 游戏id，配合marketHashName,示例值(730)
     */
    private Integer appId;
    /**
     * 可选参数，发货模式，1=人工，2自动
     */
    private Integer delivery;
    /**
     * 饰品id, 如果有这个参数，appId和marketHashName这两个参数可不传,示例值(669302600663453697)
     */
    private Long itemId;
    /**
     * 快速购买是否购买最低价，如果是1，购买最低价，
     * 如果不是，采用默认策略, 查所有小于maxPrice的在售，
     * 如果这些在售中有自动发货，那就回选最便宜的自动发货，
     * 如果没有自动发货的，就会去选一个最便宜的东西
     */
    private Double lowPrice;
    /**
     * steam饰品唯一名称，需要和appId一起传,示例值(AWP | Asiimov (Field-Tested))
     */
    private String marketHashName;
    /**
     * 购买a可以接受的最高价格(T币),示例值(3.12)
     */
    private Double maxPrice;
    /**
     * 商户订单号,示例值(12345)
     */
    private String outTradeNo;
    /**
     * 收货的steam交易链接,示例值(https://steamcommunity.com/tradeoffer/new/?partner=191976672&token=8LZ3R_AF)
     */
    private String tradeUrl;
    public C5FastPayVo(Integer appId, Integer delivery, Long itemId, Double lowPrice, String marketHashName,
                       Double maxPrice, String outTradeNo, String tradeUrl) {
        this.appId = appId;
        this.delivery = delivery;
        this.itemId = itemId;
        this.lowPrice = lowPrice;
        this.marketHashName = marketHashName;
        this.maxPrice = maxPrice;
        this.outTradeNo = outTradeNo;
        this.tradeUrl = tradeUrl;
    }
}
