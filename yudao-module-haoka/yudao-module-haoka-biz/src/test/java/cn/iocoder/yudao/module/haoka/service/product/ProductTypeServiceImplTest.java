package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductTypeDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductTypeMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link ProductTypeServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ProductTypeServiceImpl.class)
public class ProductTypeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductTypeServiceImpl productTypeService;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Test
    public void testCreateProductType_success() {
        // 准备参数
        ProductTypeSaveReqVO createReqVO = randomPojo(ProductTypeSaveReqVO.class).setId(null);

        // 调用
        Long productTypeId = productTypeService.createProductType(createReqVO);
        // 断言
        assertNotNull(productTypeId);
        // 校验记录的属性是否正确
        ProductTypeDO productType = productTypeMapper.selectById(productTypeId);
        assertPojoEquals(createReqVO, productType, "id");
    }

    @Test
    public void testUpdateProductType_success() {
        // mock 数据
        ProductTypeDO dbProductType = randomPojo(ProductTypeDO.class);
        productTypeMapper.insert(dbProductType);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductTypeSaveReqVO updateReqVO = randomPojo(ProductTypeSaveReqVO.class, o -> {
            o.setId(dbProductType.getId()); // 设置更新的 ID
        });

        // 调用
        productTypeService.updateProductType(updateReqVO);
        // 校验是否更新正确
        ProductTypeDO productType = productTypeMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productType);
    }

    @Test
    public void testUpdateProductType_notExists() {
        // 准备参数
        ProductTypeSaveReqVO updateReqVO = randomPojo(ProductTypeSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productTypeService.updateProductType(updateReqVO), PRODUCT_TYPE_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductType_success() {
        // mock 数据
        ProductTypeDO dbProductType = randomPojo(ProductTypeDO.class);
        productTypeMapper.insert(dbProductType);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductType.getId();

        // 调用
        productTypeService.deleteProductType(id);
       // 校验数据不存在了
       assertNull(productTypeMapper.selectById(id));
    }

    @Test
    public void testDeleteProductType_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productTypeService.deleteProductType(id), PRODUCT_TYPE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductTypePage() {
       // mock 数据
       ProductTypeDO dbProductType = randomPojo(ProductTypeDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setCreateTime(null);
       });
       productTypeMapper.insert(dbProductType);
       // 测试 name 不匹配
       productTypeMapper.insert(cloneIgnoreId(dbProductType, o -> o.setName(null)));
       // 测试 createTime 不匹配
       productTypeMapper.insert(cloneIgnoreId(dbProductType, o -> o.setCreateTime(null)));
       // 准备参数
       ProductTypePageReqVO reqVO = new ProductTypePageReqVO();
       reqVO.setName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ProductTypeDO> pageResult = productTypeService.getProductTypePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductType, pageResult.getList().get(0));
    }

}