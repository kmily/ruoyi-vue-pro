package cn.iocoder.yudao.module.system.dal.mysql.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.product.vo.ProductExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.product.vo.ProductPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.product.ProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ProductMapper extends BaseMapperX<ProductDO> {

    default PageResult<ProductDO> selectPage(ProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductDO>()
                .eqIfPresent(ProductDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(ProductDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(ProductDO::getProductType, reqVO.getProductType())
                .eqIfPresent(ProductDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(ProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductDO::getId));
    }

    default List<ProductDO> selectList(ProductExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProductDO>()
                .eqIfPresent(ProductDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(ProductDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(ProductDO::getProductType, reqVO.getProductType())
                .eqIfPresent(ProductDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(ProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductDO::getId));
    }

}
