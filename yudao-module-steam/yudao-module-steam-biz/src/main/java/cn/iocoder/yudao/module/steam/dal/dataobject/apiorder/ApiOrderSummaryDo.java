package cn.iocoder.yudao.module.steam.dal.dataobject.apiorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 订单扩展 DO
 *
 * @author 管理员
 */
@TableName("steam_api_order_summary")
@KeySequence("steam_api_order_summary_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiOrderSummaryDo extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 订单号，内容生成
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String merchantNo;
    /**
     * 用户编号
     */
    private Long buyUserId;
    /**
     * 用户类型
     */
    private Integer buyUserType;
    /**
     * 买方绑定用户
     */
    private Long buyBindUserId;
    /**
     *  收货方的Steam交易链接
     */
    private String buyTradeLinks;
    /**
     * 购买的steamId
     */
    private String buySteamId;
    /**
     * 卖家用户ID
     */
    private Long sellUserId;
    /**
     * 卖家用户类型
     */
    private Integer sellUserType;
    /**
     * 卖方绑定用户ID
     */
    private Long sellBindUserId;
    /**
     * 卖家steamId
     */
    private String sellSteamId;
    /**
     * 价格，单位：分
     */
    private Integer payAmount;
    /**
     * 商品总额
     */
    private Integer commodityAmount;
    /**
     * 服务费，单位分
     */
    private Integer serviceFee;
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 取消原因
     */
    private String invStatus;
    /**
     * 取消原因
     */
    private String invText;
    /**
     * 取消原因
     */
    private String shortName;
}