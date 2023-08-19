package cn.iocoder.yudao.module.product.service.sku;

import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;

/**
 * 商品库存并发 扣减 Service 接口
 *
 * @author wyz
 */
public interface ProductSkuStockService {

    /**
     * 更新 SKU 库存（增量）
     * <p>
     * 如果更新的库存不足，会抛出异常
     *
     * @param updateStockReqDTO 更行请求
     */
    void updateSkuStock(ProductSkuUpdateStockReqDTO updateStockReqDTO);
}
