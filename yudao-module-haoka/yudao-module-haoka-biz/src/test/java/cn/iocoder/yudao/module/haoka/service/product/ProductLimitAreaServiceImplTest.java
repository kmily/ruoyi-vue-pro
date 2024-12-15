package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitAreaDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitAreaMapper;
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
 * {@link ProductLimitAreaServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ProductLimitAreaServiceImpl.class)
public class ProductLimitAreaServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductLimitAreaServiceImpl productLimitAreaService;

    @Resource
    private ProductLimitAreaMapper productLimitAreaMapper;

    @Test
    public void testCreateProductLimitArea_success() {
        // 准备参数
        ProductLimitAreaSaveReqVO createReqVO = randomPojo(ProductLimitAreaSaveReqVO.class).setId(null);

        // 调用
        Long productLimitAreaId = productLimitAreaService.createProductLimitArea(createReqVO);
        // 断言
        assertNotNull(productLimitAreaId);
        // 校验记录的属性是否正确
        ProductLimitAreaDO productLimitArea = productLimitAreaMapper.selectById(productLimitAreaId);
        assertPojoEquals(createReqVO, productLimitArea, "id");
    }

    @Test
    public void testUpdateProductLimitArea_success() {
        // mock 数据
        ProductLimitAreaDO dbProductLimitArea = randomPojo(ProductLimitAreaDO.class);
        productLimitAreaMapper.insert(dbProductLimitArea);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductLimitAreaSaveReqVO updateReqVO = randomPojo(ProductLimitAreaSaveReqVO.class, o -> {
            o.setId(dbProductLimitArea.getId()); // 设置更新的 ID
        });

        // 调用
        productLimitAreaService.updateProductLimitArea(updateReqVO);
        // 校验是否更新正确
        ProductLimitAreaDO productLimitArea = productLimitAreaMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productLimitArea);
    }

    @Test
    public void testUpdateProductLimitArea_notExists() {
        // 准备参数
        ProductLimitAreaSaveReqVO updateReqVO = randomPojo(ProductLimitAreaSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productLimitAreaService.updateProductLimitArea(updateReqVO), PRODUCT_LIMIT_AREA_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductLimitArea_success() {
        // mock 数据
        ProductLimitAreaDO dbProductLimitArea = randomPojo(ProductLimitAreaDO.class);
        productLimitAreaMapper.insert(dbProductLimitArea);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductLimitArea.getId();

        // 调用
        productLimitAreaService.deleteProductLimitArea(id);
       // 校验数据不存在了
       assertNull(productLimitAreaMapper.selectById(id));
    }

    @Test
    public void testDeleteProductLimitArea_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productLimitAreaService.deleteProductLimitArea(id), PRODUCT_LIMIT_AREA_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductLimitAreaPage() {
       // mock 数据
       ProductLimitAreaDO dbProductLimitArea = randomPojo(ProductLimitAreaDO.class, o -> { // 等会查询到
           o.setHaokaProductLimitId(null);
           o.setAddressCode(null);
           o.setAddressName(null);
           o.setAllowed(null);
           o.setCreateTime(null);
       });
       productLimitAreaMapper.insert(dbProductLimitArea);
       // 测试 haokaProductLimitId 不匹配
       productLimitAreaMapper.insert(cloneIgnoreId(dbProductLimitArea, o -> o.setHaokaProductLimitId(null)));
       // 测试 addressCode 不匹配
       productLimitAreaMapper.insert(cloneIgnoreId(dbProductLimitArea, o -> o.setAddressCode(null)));
       // 测试 addressName 不匹配
       productLimitAreaMapper.insert(cloneIgnoreId(dbProductLimitArea, o -> o.setAddressName(null)));
       // 测试 allowed 不匹配
       productLimitAreaMapper.insert(cloneIgnoreId(dbProductLimitArea, o -> o.setAllowed(null)));
       // 测试 createTime 不匹配
       productLimitAreaMapper.insert(cloneIgnoreId(dbProductLimitArea, o -> o.setCreateTime(null)));
       // 准备参数
       ProductLimitAreaPageReqVO reqVO = new ProductLimitAreaPageReqVO();
       reqVO.setHaokaProductLimitId(null);
       reqVO.setAddressCode(null);
       reqVO.setAddressName(null);
       reqVO.setAllowed(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ProductLimitAreaDO> pageResult = productLimitAreaService.getProductLimitAreaPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductLimitArea, pageResult.getList().get(0));
    }

}