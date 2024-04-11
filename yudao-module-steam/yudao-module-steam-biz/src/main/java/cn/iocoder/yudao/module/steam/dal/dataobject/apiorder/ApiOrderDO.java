package cn.iocoder.yudao.module.steam.dal.dataobject.apiorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.steam.service.fin.vo.ApiQueryCommodityReqVo;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

/**
 * 新版订单 DO
 *
 * @author 管理员
 */
@TableName(value = "steam_api_order",autoResultMap = true)
@KeySequence("steam_api_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiOrderDO extends BaseDO {

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
    private String merchantOrderNo;
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
     * 购买方式
     *    枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatFormEnum 对应的类}
     */
    private Integer buyMethod;

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
     * 订单支付状态
     */
    private Integer payOrderStatus;
    /**
     * 价格，单位：分
     */
    private Integer payAmount;
    /**
     * 价格，单位：分
     */
    private String payPayRet;
    /**
     * 服务费，单位分
     */
    private Integer serviceFee;
    /**
     * 服务费率
     */
    private String serviceFeeRate;
    /**
     * 提现手续费收款钱包
     */
    private Long serviceFeeUserId;
    /**
     * 提现手续费收款人类型
     */
    private Integer serviceFeeUserType;
    /**
     * 购买平台代码
     * 枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatCodeEnum 对应的类}
     */
    private String platCode;
    /**
     * 商品总额
     */
    private Integer commodityAmount;
    /**
     * 交易阶段1,支付；2,发货，3资金结算
     */
    private Integer orderStatus;
    /**
     * 订单小状态。
     */
    private Integer orderSubStatus;
    /**
     * 发货状态
     */
    private Integer transferStatus;
    /**
     * 资金状态
     */
    private Integer cashStatus;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ApiQueryCommodityReqVo buyInfo;
    /**
     * 商品信息json
     */
    private String commodityInfo;
    /**
     * 第三方平台订单
     */
    private String threeOrderNo;
    /**
     * 取消原因
     */
    private String cancelReason;
}