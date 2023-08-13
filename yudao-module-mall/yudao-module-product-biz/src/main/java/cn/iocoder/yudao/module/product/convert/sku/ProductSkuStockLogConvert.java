package cn.iocoder.yudao.module.product.convert.sku;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuStockLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;


/**
 * 商品 SKU 库存更新日志 Convert
 *
 * @author wyz
 */
@Mapper
public interface ProductSkuStockLogConvert {

    ProductSkuStockLogConvert INSTANCE = Mappers.getMapper(ProductSkuStockLogConvert.class);


    default List<ProductSkuStockLogDO> convertList(ProductSkuUpdateStockReqDTO stockReqDTO) {
        return CollectionUtils.convertList(stockReqDTO.getItems(), INSTANCE::convert);
    }

    @Mapping(target = "skuId", source = "id")
    ProductSkuStockLogDO convert(ProductSkuUpdateStockReqDTO.Item stockReqDTO);


}
