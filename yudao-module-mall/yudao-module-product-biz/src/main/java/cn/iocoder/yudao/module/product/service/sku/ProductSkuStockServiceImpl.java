package cn.iocoder.yudao.module.product.service.sku;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.convert.sku.ProductSkuStockLogConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuStockLogDO;
import cn.iocoder.yudao.module.product.dal.mysql.sku.ProductSkuMapper;
import cn.iocoder.yudao.module.product.dal.mysql.sku.ProductSkuStockLogMapper;
import cn.iocoder.yudao.module.product.dal.redis.sku.ProductSkuStockRedisDAO;
import com.google.common.cache.CacheBuilder;
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

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.getSumValue;
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

    /**
     * 觉得这种缓存形式还是不怎么好
     */
    private final LoadingCache<Long, FutureTask<Long>> SKU_STOCK_LOCAL_LOCK = CacheBuilder.newBuilder().maximumSize(500)
            //由于没有任何计算 和 io调用，所以使用此过期方式
            .expireAfterAccess(Duration.ofMillis(5000))
            .build(new CacheLoader<Long, FutureTask<Long>>() {
                @Override
                public FutureTask<Long> load(@Nonnull Long skuId) {
                    return new FutureTask<>(() -> initRedisStock(skuId));
                }
            });

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuStock(ProductSkuUpdateStockReqDTO updateStockReqDTO) {

        List<ProductSkuStockLogDO> stockLogList = ProductSkuStockLogConvert.INSTANCE.convertList(updateStockReqDTO);
        if (CollUtil.isEmpty(stockLogList)) return;

        //保存库存更新日志
        stockLogMapper.insertBatch(stockLogList);

        //先筛选并初始化redis库存
        List<ProductSkuStockLogDO> incrList = new ArrayList<>();
        Map<Long, Long> redisStockMap = new HashMap<>();
        stockLogList.removeIf(stockLogDO -> {
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

        //增加库存
        incrementSkuStock(incrList);

        //扣减库存
        decrementSkuStock(stockLogList, redisStockMap);
    }

    private void decrementSkuStock(List<ProductSkuStockLogDO> decrList, Map<Long, Long> redisStockMap) {
        if (CollUtil.isEmpty(decrList)) return;
        //根据扣减完 redis剩余库存从小到大排序
        decrList.sort(Comparator.comparing(o -> redisStockMap.get(o.getSkuId()) - o.getIncrCount()));

        //进行库存扣减
        List<ProductSkuStockLogDO> successDecrList = new ArrayList<>();
        Throwable ex = null;
        try {
            decrList.forEach(stockLogDO -> {
                successDecrList.add(stockLogDO);
                Long redisStock = stockRedisDAO.decrementStock(stockLogDO.getSkuId(), stockLogDO.getIncrCount());
                if (redisStock < 0) {
                    throw exception(SKU_STOCK_NOT_ENOUGH);
                }
            });
        } catch (Throwable e) {
            ex = e;
        }

        if (ex == null) return; //全部成功, 返回

        //出现库存扣减失败或者异常，需要回填库存
        successDecrList.forEach(stockLogDO -> stockRedisDAO.incrementStock(stockLogDO.getSkuId(), stockLogDO.getIncrCount()));

        //抛出异常
        if (ex instanceof ServiceException) {
            throw (ServiceException) ex;
        }

        log.error("[decrementSkuStock][扣减库存时redis异常]", ex);
        throw exception(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    private void incrementSkuStock(List<ProductSkuStockLogDO> incrList) {
        if (CollUtil.isEmpty(incrList)) return;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //事务完成后，再进行库存回增，避免超卖
                incrList.forEach(stockLogDO -> stockRedisDAO.incrementStock(stockLogDO.getSkuId(), -stockLogDO.getIncrCount()));
            }
        });
    }


    /**
     * 定时任务来调用
     */
    @SuppressWarnings("unused")
    public void syncStock() {
        Set<Long> skuIds = convertSet(stockLogMapper.selectList(), ProductSkuStockLogDO::getSkuId);
        skuIds.forEach(skuId -> getSelf().doSyncStock(skuId));
        //spu的库存同步怎么处理好，感觉spu的库存与实际下单扣减sku库存没关系，可以在需要spu库存的查询或者统计的时候进行处理
    }

    private Long obtainRedisStock(Long skuId) {
        return ObjectUtil.defaultIfNull(stockRedisDAO.getStock(skuId), () -> syncObtainRedisStock(skuId));
    }

    private Long syncObtainRedisStock(Long skuId) {
        try {
            // 针对单个服务的 并发限制
            FutureTask<Long> futureTask = SKU_STOCK_LOCAL_LOCK.get(skuId);
            futureTask.run();
            return futureTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            if (!(e instanceof ServiceException)) {
                log.error("[syncObtainRedisStock][初始化同步redis库存异常]", e);
            }
            return 0L;
        }
    }

    /**
     * 当redis缓存为空时来调用
     * 初始化指定skuId对应的缓存并存入到redis中
     *
     * @param skuId 商品skuId
     * @return 返回redis中指定skuId存储的 库存数量
     */
    private Long initRedisStock(Long skuId) throws Exception {
        // 针对微服务的 并发限制
        return stockRedisDAO.syncInitRedisStock(skuId, 1000, () -> getSelf().doSyncStock(skuId));
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
        Integer sumValue = ObjectUtil.defaultIfNull(getSumValue(stockLogList, ProductSkuStockLogDO::getIncrCount, Integer::sum), 0);

        Integer dbStock = productSkuDO.getStock();
        if (sumValue != 0) {
            if (dbStock - sumValue < 0) {
                log.warn("[doSyncStock][进行库存同步时，商品skuId({}) 最终的库存数为小于0，发生超卖情况]", skuId);
            }

            LambdaQueryWrapperX<ProductSkuDO> eq = new LambdaQueryWrapperX<ProductSkuDO>().eq(ProductSkuDO::getId, skuId).eq(ProductSkuDO::getStock, dbStock);
            if (productSkuMapper.update(new ProductSkuDO().setStock(dbStock - sumValue), eq) == 0) {
                throw exception(new ErrorCode(1008006005, "库存更新失败"));
            }
        }

        if (CollUtil.isNotEmpty(stockLogList)) {
            stockLogMapper.deleteBatchIds(CollectionUtils.convertList(stockLogList, ProductSkuStockLogDO::getId));
        }

        return (long) (dbStock - sumValue);
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
