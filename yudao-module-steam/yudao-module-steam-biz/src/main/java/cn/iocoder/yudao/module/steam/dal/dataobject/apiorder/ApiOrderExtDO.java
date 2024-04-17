package cn.iocoder.yudao.module.steam.dal.dataobject.apiorder;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
/**
 * 订单扩展 DO
 *
 * @author 管理员
 */
@TableName("steam_api_order_ext")
@KeySequence("steam_api_order_ext_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiOrderExtDO extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 购买平台代码
     * 枚举 {@link cn.iocoder.yudao.module.steam.enums.PlatCodeEnum 对应的类}
     */
    private String platCode;
    /**
     * 第三方平台订单号
     */
    private String orderNo;
    /**
     * 第三方平台订单号
     * 各个平台自主生成
     */
    private String merchantNo;
    /**
     * 报价ID,一般保存steam的
     */
    private Long tradeOfferId;
    /**
     * 报价链接
     */
    private String tradeOfferLinks;
    /**
     * 主订单号
     */
    private Long orderId;
    /**
     * 订单信息，json
     */
    private String orderInfo;
    /**
     * 商品信息json
     */
    private String commodityInfo;
    /**
     * 交易阶段1,进行中，2完成，3作废
     */
    private Integer orderStatus;
    /**
     * 平台订单小状态描述。
     */
    private String orderSubStatus;
    /**
     * 平台订单小状态描述。
     */
    private String orderSubStatusErrText;
}