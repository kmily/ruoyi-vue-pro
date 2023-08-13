package cn.iocoder.yudao.module.product.dal.redis;


/**
 * product Redis Key 枚举类
 *
 * @author wyz
 */
public interface ProductRedisKeyConstants {


    String PRODUCT_SKU_STOCK = "product_sku_stock:%s";
    String PRODUCT_SKU_STOCK_LOCK = "product_sku_stock_lock:%s";
}
