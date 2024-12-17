package cn.iocoder.yudao.module.haoka.service.onsaleproduct;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.onsaleproduct.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.onsaleproduct.OnSaleProductDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.onsaleproduct.OnSaleProductMapper;
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
 * {@link OnSaleProductServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(OnSaleProductServiceImpl.class)
public class OnSaleProductServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OnSaleProductServiceImpl onSaleProductService;

    @Resource
    private OnSaleProductMapper onSaleProductMapper;

    @Test
    public void testCreateOnSaleProduct_success() {
        // 准备参数
        OnSaleProductSaveReqVO createReqVO = randomPojo(OnSaleProductSaveReqVO.class).setId(null);

        // 调用
        Long onSaleProductId = onSaleProductService.createOnSaleProduct(createReqVO);
        // 断言
        assertNotNull(onSaleProductId);
        // 校验记录的属性是否正确
        OnSaleProductDO onSaleProduct = onSaleProductMapper.selectById(onSaleProductId);
        assertPojoEquals(createReqVO, onSaleProduct, "id");
    }

    @Test
    public void testUpdateOnSaleProduct_success() {
        // mock 数据
        OnSaleProductDO dbOnSaleProduct = randomPojo(OnSaleProductDO.class);
        onSaleProductMapper.insert(dbOnSaleProduct);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OnSaleProductSaveReqVO updateReqVO = randomPojo(OnSaleProductSaveReqVO.class, o -> {
            o.setId(dbOnSaleProduct.getId()); // 设置更新的 ID
        });

        // 调用
        onSaleProductService.updateOnSaleProduct(updateReqVO);
        // 校验是否更新正确
        OnSaleProductDO onSaleProduct = onSaleProductMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, onSaleProduct);
    }

    @Test
    public void testUpdateOnSaleProduct_notExists() {
        // 准备参数
        OnSaleProductSaveReqVO updateReqVO = randomPojo(OnSaleProductSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> onSaleProductService.updateOnSaleProduct(updateReqVO), ON_SALE_PRODUCT_NOT_EXISTS);
    }

    @Test
    public void testDeleteOnSaleProduct_success() {
        // mock 数据
        OnSaleProductDO dbOnSaleProduct = randomPojo(OnSaleProductDO.class);
        onSaleProductMapper.insert(dbOnSaleProduct);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOnSaleProduct.getId();

        // 调用
        onSaleProductService.deleteOnSaleProduct(id);
       // 校验数据不存在了
       assertNull(onSaleProductMapper.selectById(id));
    }

    @Test
    public void testDeleteOnSaleProduct_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> onSaleProductService.deleteOnSaleProduct(id), ON_SALE_PRODUCT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOnSaleProductPage() {
       // mock 数据
       OnSaleProductDO dbOnSaleProduct = randomPojo(OnSaleProductDO.class, o -> { // 等会查询到
           o.setParentProductId(null);
           o.setName(null);
           o.setSku(null);
           o.setOnSale(null);
           o.setCreateTime(null);
       });
       onSaleProductMapper.insert(dbOnSaleProduct);
       // 测试 parentProductId 不匹配
       onSaleProductMapper.insert(cloneIgnoreId(dbOnSaleProduct, o -> o.setParentProductId(null)));
       // 测试 name 不匹配
       onSaleProductMapper.insert(cloneIgnoreId(dbOnSaleProduct, o -> o.setName(null)));
       // 测试 sku 不匹配
       onSaleProductMapper.insert(cloneIgnoreId(dbOnSaleProduct, o -> o.setSku(null)));
       // 测试 onSale 不匹配
       onSaleProductMapper.insert(cloneIgnoreId(dbOnSaleProduct, o -> o.setOnSale(null)));
       // 测试 createTime 不匹配
       onSaleProductMapper.insert(cloneIgnoreId(dbOnSaleProduct, o -> o.setCreateTime(null)));
       // 准备参数
       OnSaleProductPageReqVO reqVO = new OnSaleProductPageReqVO();
       reqVO.setParentProductId(null);
       reqVO.setName(null);
       reqVO.setSku(null);
       reqVO.setOnSale(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<OnSaleProductDO> pageResult = onSaleProductService.getOnSaleProductPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOnSaleProduct, pageResult.getList().get(0));
    }

}