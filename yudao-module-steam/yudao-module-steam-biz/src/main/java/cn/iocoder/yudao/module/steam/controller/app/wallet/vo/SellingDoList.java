package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.steam.service.steam.TransferMsg;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@TableName(value = "steam_inv_order",autoResultMap = true)
@KeySequence("steam_inv_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellingDoList extends PageParam implements Serializable {

    /**
     * 订单编号
     */
    @TableId
//    private Long id;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 商品价格，单位：分
     */
    private Integer commodityAmount;
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

}
