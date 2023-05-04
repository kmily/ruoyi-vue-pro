package cn.iocoder.yudao.module.oa.convert.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.product.vo.ProductCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.product.vo.ProductExcelVO;
import cn.iocoder.yudao.module.oa.controller.admin.product.vo.ProductRespVO;
import cn.iocoder.yudao.module.oa.controller.admin.product.vo.ProductUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.product.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 产品 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ProductConvert {

    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

    ProductDO convert(ProductCreateReqVO bean);

    ProductDO convert(ProductUpdateReqVO bean);

    ProductRespVO convert(ProductDO bean);

    List<ProductRespVO> convertList(List<ProductDO> list);

    PageResult<ProductRespVO> convertPage(PageResult<ProductDO> page);

    List<ProductExcelVO> convertList02(List<ProductDO> list);

}
