package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductLimitCardDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductLimitCardMapper;
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
 * {@link ProductLimitCardServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ProductLimitCardServiceImpl.class)
public class ProductLimitCardServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductLimitCardServiceImpl productLimitCardService;

    @Resource
    private ProductLimitCardMapper productLimitCardMapper;

    @Test
    public void testCreateProductLimitCard_success() {
        // 准备参数
        ProductLimitCardSaveReqVO createReqVO = randomPojo(ProductLimitCardSaveReqVO.class).setId(null);

        // 调用
        Long productLimitCardId = productLimitCardService.createProductLimitCard(createReqVO);
        // 断言
        assertNotNull(productLimitCardId);
        // 校验记录的属性是否正确
        ProductLimitCardDO productLimitCard = productLimitCardMapper.selectById(productLimitCardId);
        assertPojoEquals(createReqVO, productLimitCard, "id");
    }

    @Test
    public void testUpdateProductLimitCard_success() {
        // mock 数据
        ProductLimitCardDO dbProductLimitCard = randomPojo(ProductLimitCardDO.class);
        productLimitCardMapper.insert(dbProductLimitCard);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductLimitCardSaveReqVO updateReqVO = randomPojo(ProductLimitCardSaveReqVO.class, o -> {
            o.setId(dbProductLimitCard.getId()); // 设置更新的 ID
        });

        // 调用
        productLimitCardService.updateProductLimitCard(updateReqVO);
        // 校验是否更新正确
        ProductLimitCardDO productLimitCard = productLimitCardMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productLimitCard);
    }

    @Test
    public void testUpdateProductLimitCard_notExists() {
        // 准备参数
        ProductLimitCardSaveReqVO updateReqVO = randomPojo(ProductLimitCardSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productLimitCardService.updateProductLimitCard(updateReqVO), PRODUCT_LIMIT_CARD_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductLimitCard_success() {
        // mock 数据
        ProductLimitCardDO dbProductLimitCard = randomPojo(ProductLimitCardDO.class);
        productLimitCardMapper.insert(dbProductLimitCard);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductLimitCard.getId();

        // 调用
        productLimitCardService.deleteProductLimitCard(id);
       // 校验数据不存在了
       assertNull(productLimitCardMapper.selectById(id));
    }

    @Test
    public void testDeleteProductLimitCard_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productLimitCardService.deleteProductLimitCard(id), PRODUCT_LIMIT_CARD_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductLimitCardPage() {
       // mock 数据
       ProductLimitCardDO dbProductLimitCard = randomPojo(ProductLimitCardDO.class, o -> { // 等会查询到
           o.setHaokaProductLimitId(null);
           o.setCardNum(null);
           o.setCreateTime(null);
       });
       productLimitCardMapper.insert(dbProductLimitCard);
       // 测试 haokaProductLimitId 不匹配
       productLimitCardMapper.insert(cloneIgnoreId(dbProductLimitCard, o -> o.setHaokaProductLimitId(null)));
       // 测试 cardNum 不匹配
       productLimitCardMapper.insert(cloneIgnoreId(dbProductLimitCard, o -> o.setCardNum(null)));
       // 测试 createTime 不匹配
       productLimitCardMapper.insert(cloneIgnoreId(dbProductLimitCard, o -> o.setCreateTime(null)));
       // 准备参数
       ProductLimitCardPageReqVO reqVO = new ProductLimitCardPageReqVO();
       reqVO.setHaokaProductLimitId(null);
       reqVO.setCardNum(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ProductLimitCardDO> pageResult = productLimitCardService.getProductLimitCardPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductLimitCard, pageResult.getList().get(0));
    }

}