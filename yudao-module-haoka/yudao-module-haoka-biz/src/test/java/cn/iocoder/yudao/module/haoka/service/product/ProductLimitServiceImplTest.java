package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitMapper;
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
 * {@link ProductLimitServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ProductLimitServiceImpl.class)
public class ProductLimitServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductLimitServiceImpl productLimitService;

    @Resource
    private ProductLimitMapper productLimitMapper;

    @Test
    public void testCreateProductLimit_success() {
        // 准备参数
        ProductLimitSaveReqVO createReqVO = randomPojo(ProductLimitSaveReqVO.class).setId(null);

        // 调用
        Long productLimitId = productLimitService.createProductLimit(createReqVO);
        // 断言
        assertNotNull(productLimitId);
        // 校验记录的属性是否正确
        ProductLimitDO productLimit = productLimitMapper.selectById(productLimitId);
        assertPojoEquals(createReqVO, productLimit, "id");
    }

    @Test
    public void testUpdateProductLimit_success() {
        // mock 数据
        ProductLimitDO dbProductLimit = randomPojo(ProductLimitDO.class);
        productLimitMapper.insert(dbProductLimit);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductLimitSaveReqVO updateReqVO = randomPojo(ProductLimitSaveReqVO.class, o -> {
            o.setId(dbProductLimit.getId()); // 设置更新的 ID
        });

        // 调用
        productLimitService.updateProductLimit(updateReqVO);
        // 校验是否更新正确
        ProductLimitDO productLimit = productLimitMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productLimit);
    }

    @Test
    public void testUpdateProductLimit_notExists() {
        // 准备参数
        ProductLimitSaveReqVO updateReqVO = randomPojo(ProductLimitSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productLimitService.updateProductLimit(updateReqVO), PRODUCT_LIMIT_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductLimit_success() {
        // mock 数据
        ProductLimitDO dbProductLimit = randomPojo(ProductLimitDO.class);
        productLimitMapper.insert(dbProductLimit);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductLimit.getId();

        // 调用
        productLimitService.deleteProductLimit(id);
       // 校验数据不存在了
       assertNull(productLimitMapper.selectById(id));
    }

    @Test
    public void testDeleteProductLimit_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productLimitService.deleteProductLimit(id), PRODUCT_LIMIT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductLimitPage() {
       // mock 数据
       ProductLimitDO dbProductLimit = randomPojo(ProductLimitDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setUseOnlySendArea(null);
           o.setUseNotSendArea(null);
           o.setUseCardLimit(null);
           o.setAgeMax(null);
           o.setAgeMin(null);
           o.setPersonCardQuantityLimit(null);
           o.setDetectionCycle(null);
           o.setCreateTime(null);
       });
       productLimitMapper.insert(dbProductLimit);
       // 测试 name 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setName(null)));
       // 测试 useOnlySendArea 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setUseOnlySendArea(null)));
       // 测试 useNotSendArea 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setUseNotSendArea(null)));
       // 测试 useCardLimit 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setUseCardLimit(null)));
       // 测试 ageMax 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setAgeMax(null)));
       // 测试 ageMin 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setAgeMin(null)));
       // 测试 personCardQuantityLimit 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setPersonCardQuantityLimit(null)));
       // 测试 detectionCycle 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setDetectionCycle(null)));
       // 测试 createTime 不匹配
       productLimitMapper.insert(cloneIgnoreId(dbProductLimit, o -> o.setCreateTime(null)));
       // 准备参数
       ProductLimitPageReqVO reqVO = new ProductLimitPageReqVO();
       reqVO.setName(null);
       reqVO.setUseOnlySendArea(null);
       reqVO.setUseNotSendArea(null);
       reqVO.setUseCardLimit(null);
       reqVO.setAgeMax(null);
       reqVO.setAgeMin(null);
       reqVO.setPersonCardQuantityLimit(null);
       reqVO.setDetectionCycle(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ProductLimitDO> pageResult = productLimitService.getProductLimitPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductLimit, pageResult.getList().get(0));
    }

}