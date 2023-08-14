package cn.iocoder.yudao.module.product.dal.redis.sku;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuStockLogDO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.dal.redis.ProductRedisKeyConstants.PRODUCT_SKU_STOCK;
import static cn.iocoder.yudao.module.product.dal.redis.ProductRedisKeyConstants.PRODUCT_SKU_STOCK_LOCK;


/**
 * {@link ProductSkuStockLogDO} 的 RedisDAO
 *
 * @author wyz
 */
@Repository
public class ProductSkuStockRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    private static final int SKU_STOCK_EXPIRE_TIME = 300 * 1000; //5分钟过期

    private String formatKey(Long skuId) {
        return String.format(PRODUCT_SKU_STOCK, skuId.toString());
    }

    public void incrementStock(Long skuId, long delta) {
        Assert.isTrue(delta > 0);
        stringRedisTemplate.opsForValue().increment(formatKey(skuId), delta);
    }

    public Long decrementStock(Long skuId, long delta) {
        Assert.isTrue(delta > 0);
        return stringRedisTemplate.opsForValue().decrement(formatKey(skuId), delta);
    }


    public Long getStock(Long skuId) {
        String stockStr = stringRedisTemplate.opsForValue().getAndExpire(formatKey(skuId), SKU_STOCK_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return StrUtil.isEmpty(stockStr) ? null : Long.parseLong(stockStr);
    }

    public void setStock(Long skuId, Long stock) {
        stringRedisTemplate.opsForValue().set(formatKey(skuId), stock.toString(), SKU_STOCK_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }


    private static String formatLockKey(Long skuId) {
        return String.format(PRODUCT_SKU_STOCK_LOCK, skuId.toString());
    }

    public Long syncInitRedisStock(Long skuId, long timeoutMillis, Supplier<Long> supplier) throws Exception {
        String lockKey = formatLockKey(skuId);
        RLock lock = redissonClient.getLock(lockKey);
        boolean agree = false;
        try {
            agree = lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
            if (agree) { // 执行逻辑
                Long stock = getStock(skuId);
                if (stock != null) return stock; //存在立即返回

                stock = supplier.get(); //获取同步后的库存
                setStock(skuId, stock); //设置缓存
                return stock;
            } else {
                throw exception(GlobalErrorCodeConstants.LOCKED);
            }
        } finally {
            if (agree) lock.unlock();
        }
    }


}
