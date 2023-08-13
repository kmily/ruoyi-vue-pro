package cn.iocoder.yudao.module.product.dal.dataobject.sku;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


/**
 * 商品 SKU 库存更新日志 DO
 *
 * @author wyz
 */
@TableName(value = "product_sku_stock_log", autoResultMap = true)
@KeySequence("product_sku_stock_log") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuStockLogDO extends BaseDO {

    /**
     * 商品 SKU 编号，自增
     */
    @TableId
    private Long id;

    /**
     * SPU 编号
     * <p>
     * 关联 {@link ProductSkuDO#getId()}
     */
    private Long skuId;

    /**
     * 库存更新数
     */
    private Integer incrCount;


}

