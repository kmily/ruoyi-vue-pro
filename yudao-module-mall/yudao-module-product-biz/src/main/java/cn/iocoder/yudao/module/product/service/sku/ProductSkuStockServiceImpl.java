package cn.iocoder.yudao.module.product.service.sku;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.util.cache.CacheUtils;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.convert.sku.ProductSkuStockLogConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuStockLogDO;
import cn.iocoder.yudao.module.product.dal.mysql.sku.ProductSkuMapper;
import cn.iocoder.yudao.module.product.dal.mysql.sku.ProductSkuStockLogMapper;
import cn.iocoder.yudao.module.product.dal.redis.sku.ProductSkuStockRedisDAO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SKU_STOCK_NOT_ENOUGH;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SPU_NOT_EXISTS;

/**
 * 商品 库存并发扣减 Service 实现类
 *
 * @author wyz
 */
@Slf4j
@Service
@Validated
public class ProductSkuStockServiceImpl implements ProductSkuStockService {


    @Resource
    private ProductSkuStockLogMapper stockLogMapper;
    @Resource
    private ProductSkuStockRedisDAO stockRedisDAO;
    @Resource
    private ProductSkuMapper productSkuMapper;

    private final LoadingCache<Long, FutureTask<Long>> SKU_STOCK_LOCAL_LOCK = CacheUtils.buildAsyncReloadingCache(Duration.ofSeconds(10), new CacheLoader<Long, FutureTask<Long>>() {
        @Override
        public FutureTask<Long> load(Long skuId) {
            return new FutureTask<>(() -> initRedisStock(skuId));
        }
    });

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuStock(ProductSkuUpdateStockReqDTO updateStockReqDTO) {

        List<ProductSkuStockLogDO> stockLogList = ProductSkuStockLogConvert.INSTANCE.convertList(updateStockReqDTO);
        //传进来的 列表里面的id应该是去重过的吧，如果没有需要去重合并更新数
        if (CollUtil.isEmpty(stockLogList)) return;

        //保存库存更新日志
        stockLogMapper.insertBatch(stockLogList);

        //先筛选出 可以扣减的 skuLog
        List<ProductSkuStockLogDO> incrList = new ArrayList<>();
        Map<Long, Long> redisStockMap = new HashMap<>();
        stockLogList.removeIf(stockLogDO -> {
            // 先进行redis库存获取，也是初始化redis库存
            Long redisStock = obtainRedisStock(stockLogDO.getSkuId());
            if (stockLogDO.getIncrCount() == 0) return true; //无需操作
            if (stockLogDO.getIncrCount() < 0) {
                incrList.add(stockLogDO);
                return true;
            }

            if (stockLogDO.getIncrCount() > redisStock) {
                throw exception(SKU_STOCK_NOT_ENOUGH);
            }

            redisStockMap.put(stockLogDO.getSkuId(), redisStock);
            return false;
        });

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //事务完成后，再进行库存回增，避免超卖
                incrList.forEach(stockLogDO -> stockRedisDAO.incrementStock(stockLogDO.getSkuId(), -stockLogDO.getIncrCount()));
            }
        });

        //根据扣减完 redis剩余库存从小到大排序
        if (CollUtil.isEmpty(stockLogList)) return;
        stockLogList.sort(Comparator.comparing(o -> redisStockMap.get(o.getSkuId()) - o.getIncrCount()));
        boolean isAllSuccess = true;

        //进行库存扣减
        List<ProductSkuStockLogDO> successDecrList = new ArrayList<>();
        for (ProductSkuStockLogDO stockLogDO : stockLogList) {
            successDecrList.add(stockLogDO);
            try {
                Long redisStock = stockRedisDAO.decrementStock(stockLogDO.getSkuId(), stockLogDO.getIncrCount());
                if (redisStock < 0) {
                    isAllSuccess = false;
                    break;
                }
            } catch (Throwable ignore) {
                isAllSuccess = false;
                break;
            }
        }

        if (isAllSuccess) return; //全部成功, 返回

        //出现库存扣减失败或者异常，需要回填库存
        successDecrList.forEach(stockLogDO -> stockRedisDAO.incrementStock(stockLogDO.getSkuId(), stockLogDO.getIncrCount()));

        //抛出异常
        throw exception(SKU_STOCK_NOT_ENOUGH);
    }


    /**
     * 定时任务来调用
     */
    public void syncStock() {
        Set<Long> skuIds = convertSet(stockLogMapper.selectList(), ProductSkuStockLogDO::getSkuId);
        skuIds.forEach(skuId -> {
            // 交给线程池处理，
            getSelf().doSyncStock(skuId);
        });

        //spu的库存同步怎么处理好，感觉spu的库存与实际下单扣减sku库存没关系，可以在需要spu库存的查询或者统计的时候进行处理
    }

    private Long obtainRedisStock(Long skuId) {
        return ObjectUtil.defaultIfNull(stockRedisDAO.getStock(skuId), syncObtainRedisStock(skuId));
    }

    private Long syncObtainRedisStock(Long skuId) {
        try {
            // 针对单个服务的 并发限制
            FutureTask<Long> futureTask = SKU_STOCK_LOCAL_LOCK.get(skuId);
            futureTask.run();
            return futureTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            //抛错，防超卖
            throw exception(SKU_STOCK_NOT_ENOUGH);
        }
    }

    /**
     * 当redis缓存为空时来调用
     * 初始化指定skuId对应的缓存并存入到redis中
     *
     * @param skuId 商品skuId
     * @return 返回redis中指定skuId存储的 库存数量
     */
    private Long initRedisStock(Long skuId) {
        AtomicReference<Long> redisStock = new AtomicReference<>(0L);
        stockRedisDAO.lock(skuId, 1000L, () -> {
            // 针对微服务的 并发限制
            Long stock = stockRedisDAO.getStock(skuId);
            if (stock != null) {
                redisStock.set(stock);
                return;
            }
            Long syncStock = getSelf().doSyncStock(skuId);
            stockRedisDAO.setStock(skuId, syncStock);
            redisStock.set(syncStock);
        });
        return redisStock.get();
    }

    /**
     * 同步单个skuId在数据库中库存 并返回库存
     *
     * @param skuId 商品skuId
     * @return 执行同步后的sku 数据库库存
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Long doSyncStock(Long skuId) {
        ProductSkuDO productSkuDO = productSkuMapper.selectById(skuId);
        if (productSkuDO == null) {
            throw exception(SPU_NOT_EXISTS);
        }

        List<ProductSkuStockLogDO> stockLogList = stockLogMapper.selectList(skuId);
        Integer sumValue = CollectionUtils.getSumValue(stockLogList, ProductSkuStockLogDO::getIncrCount, Integer::sum);

        Integer syncStock = productSkuDO.getStock();
        if (sumValue != null && sumValue != 0) {
            syncStock = syncStock - sumValue;
            if (syncStock < 0) {
                log.warn("[doSyncStock][进行库存同步时，商品skuId({}) 最终的库存数为小于0，发生超卖情况]", skuId);
            }

            LambdaUpdateWrapper<ProductSkuDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ProductSkuDO>()
                    .setSql(" stock = stock - " + sumValue)
                    .eq(ProductSkuDO::getId, skuId);
            productSkuMapper.update(null, lambdaUpdateWrapper);
        }

        if (sumValue != null) {
            stockLogMapper.deleteBatchIds(CollectionUtils.convertList(stockLogList, ProductSkuStockLogDO::getId));
        }

        return syncStock.longValue();
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private ProductSkuStockServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }


}
