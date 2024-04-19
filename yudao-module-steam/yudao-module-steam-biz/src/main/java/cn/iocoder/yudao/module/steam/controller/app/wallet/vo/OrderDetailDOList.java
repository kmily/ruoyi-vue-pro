package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName(value = "steam_inv_order",autoResultMap = true)
@KeySequence("steam_inv_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDOList {

    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 订单收货时间
     */
    private LocalDateTime updateTime;
    /**
     * 总价
     */
    private Integer commodityAmount;
    /**
     * 实际价格
     */
    private Integer payAmount;
    /**
     * 服务费
     */
    private Integer serviceFee;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String merchantNo;
    /**
     * 商品名称
     */
    private String marketName;
    /**
     * 商品hash
     */
    private String marketHashName;

    /**
     * 商品状态
     */
    private Integer transferStatus;

    /**
     * 付款状态
     */
    private Integer payStatus;
    /**
     * 商品图片
     */
    private String iconUrl;

    /**
     * 商品外观
     */
    private String selExterior;

    /**
     * 商品颜色
     */
    private String selExteriorColor;

    /**
     * 买家steamId
     */
    private Integer buySteamId;

    /**
     * 卖家steamId
     */
    private Integer sellSteamId;

    /**
     * 买家头像
     */
    private Integer buyAvatar;

    /**
     * 卖家头像
     */
    private Integer sellAvatar;

    /**
     * 买家昵称
     */
    private Integer buySteamName;

    /**
     * 卖家头像
     */
    private Integer sellSteamName;

}
