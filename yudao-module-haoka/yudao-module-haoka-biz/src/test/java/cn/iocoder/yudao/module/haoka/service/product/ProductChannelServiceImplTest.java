package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.ProductChannelDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.ProductChannelMapper;
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
 * {@link ProductChannelServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ProductChannelServiceImpl.class)
public class ProductChannelServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductChannelServiceImpl productChannelService;

    @Resource
    private ProductChannelMapper productChannelMapper;

    @Test
    public void testCreateProductChannel_success() {
        // 准备参数
        ProductChannelSaveReqVO createReqVO = randomPojo(ProductChannelSaveReqVO.class).setId(null);

        // 调用
        Long productChannelId = productChannelService.createProductChannel(createReqVO);
        // 断言
        assertNotNull(productChannelId);
        // 校验记录的属性是否正确
        ProductChannelDO productChannel = productChannelMapper.selectById(productChannelId);
        assertPojoEquals(createReqVO, productChannel, "id");
    }

    @Test
    public void testUpdateProductChannel_success() {
        // mock 数据
        ProductChannelDO dbProductChannel = randomPojo(ProductChannelDO.class);
        productChannelMapper.insert(dbProductChannel);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductChannelSaveReqVO updateReqVO = randomPojo(ProductChannelSaveReqVO.class, o -> {
            o.setId(dbProductChannel.getId()); // 设置更新的 ID
        });

        // 调用
        productChannelService.updateProductChannel(updateReqVO);
        // 校验是否更新正确
        ProductChannelDO productChannel = productChannelMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productChannel);
    }

    @Test
    public void testUpdateProductChannel_notExists() {
        // 准备参数
        ProductChannelSaveReqVO updateReqVO = randomPojo(ProductChannelSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productChannelService.updateProductChannel(updateReqVO), PRODUCT_CHANNEL_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductChannel_success() {
        // mock 数据
        ProductChannelDO dbProductChannel = randomPojo(ProductChannelDO.class);
        productChannelMapper.insert(dbProductChannel);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductChannel.getId();

        // 调用
        productChannelService.deleteProductChannel(id);
       // 校验数据不存在了
       assertNull(productChannelMapper.selectById(id));
    }

    @Test
    public void testDeleteProductChannel_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productChannelService.deleteProductChannel(id), PRODUCT_CHANNEL_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductChannelPage() {
       // mock 数据
       ProductChannelDO dbProductChannel = randomPojo(ProductChannelDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setCreateTime(null);
       });
       productChannelMapper.insert(dbProductChannel);
       // 测试 name 不匹配
       productChannelMapper.insert(cloneIgnoreId(dbProductChannel, o -> o.setName(null)));
       // 测试 createTime 不匹配
       productChannelMapper.insert(cloneIgnoreId(dbProductChannel, o -> o.setCreateTime(null)));
       // 准备参数
       ProductChannelPageReqVO reqVO = new ProductChannelPageReqVO();
       reqVO.setName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ProductChannelDO> pageResult = productChannelService.getProductChannelPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductChannel, pageResult.getList().get(0));
    }

}