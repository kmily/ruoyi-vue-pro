package cn.iocoder.yudao.module.product.dal.mysql.sku;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuStockLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSkuStockLogMapper extends BaseMapperX<ProductSkuStockLogDO> {


    default List<ProductSkuStockLogDO> selectList(Long skuId) {
        return selectList(new LambdaQueryWrapperX<ProductSkuStockLogDO>().eq(ProductSkuStockLogDO::getSkuId, skuId));
    }
}
