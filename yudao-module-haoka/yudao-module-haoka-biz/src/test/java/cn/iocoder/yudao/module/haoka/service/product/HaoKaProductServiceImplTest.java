package cn.iocoder.yudao.module.haoka.service.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.product.HaoKaProductDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.product.HaoKaProductMapper;
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
 * {@link HaoKaProductServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(HaoKaProductServiceImpl.class)
public class HaoKaProductServiceImplTest extends BaseDbUnitTest {

    @Resource
    private HaoKaProductServiceImpl haoKaProductService;

    @Resource
    private HaoKaProductMapper haoKaProductMapper;

    @Test
    public void testCreateHaoKaProduct_success() {
        // 准备参数
        HaoKaProductSaveReqVO createReqVO = randomPojo(HaoKaProductSaveReqVO.class).setId(null);

        // 调用
        Long haoKaProductId = haoKaProductService.createHaoKaProduct(createReqVO);
        // 断言
        assertNotNull(haoKaProductId);
        // 校验记录的属性是否正确
        HaoKaProductDO haoKaProduct = haoKaProductMapper.selectById(haoKaProductId);
        assertPojoEquals(createReqVO, haoKaProduct, "id");
    }

    @Test
    public void testUpdateHaoKaProduct_success() {
        // mock 数据
        HaoKaProductDO dbHaoKaProduct = randomPojo(HaoKaProductDO.class);
        haoKaProductMapper.insert(dbHaoKaProduct);// @Sql: 先插入出一条存在的数据
        // 准备参数
        HaoKaProductSaveReqVO updateReqVO = randomPojo(HaoKaProductSaveReqVO.class, o -> {
            o.setId(dbHaoKaProduct.getId()); // 设置更新的 ID
        });

        // 调用
        haoKaProductService.updateHaoKaProduct(updateReqVO);
        // 校验是否更新正确
        HaoKaProductDO haoKaProduct = haoKaProductMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, haoKaProduct);
    }

    @Test
    public void testUpdateHaoKaProduct_notExists() {
        // 准备参数
        HaoKaProductSaveReqVO updateReqVO = randomPojo(HaoKaProductSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> haoKaProductService.updateHaoKaProduct(updateReqVO), HAO_KA_PRODUCT_NOT_EXISTS);
    }

    @Test
    public void testDeleteHaoKaProduct_success() {
        // mock 数据
        HaoKaProductDO dbHaoKaProduct = randomPojo(HaoKaProductDO.class);
        haoKaProductMapper.insert(dbHaoKaProduct);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbHaoKaProduct.getId();

        // 调用
        haoKaProductService.deleteHaoKaProduct(id);
       // 校验数据不存在了
       assertNull(haoKaProductMapper.selectById(id));
    }

    @Test
    public void testDeleteHaoKaProduct_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> haoKaProductService.deleteHaoKaProduct(id), HAO_KA_PRODUCT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetHaoKaProductPage() {
       // mock 数据
       HaoKaProductDO dbHaoKaProduct = randomPojo(HaoKaProductDO.class, o -> { // 等会查询到
           o.setOperator(null);
           o.setSku(null);
           o.setName(null);
           o.setHaokaProductTypeId(null);
           o.setBelongAreaCode(null);
           o.setHaokaProductChannelId(null);
           o.setHaokaProductLimitId(null);
           o.setIdCardNumVerify(null);
           o.setIdCardImgVerify(null);
           o.setProduceAddress(null);
           o.setNeedBlacklistFilter(null);
           o.setEnableStockLimit(null);
           o.setStockNum(null);
           o.setStockWarnNum(null);
           o.setProduceRemarks(null);
           o.setSettlementRules(null);
           o.setEstimatedRevenue(null);
           o.setOnSale(null);
           o.setIsTop(null);
           o.setCreateTime(null);
       });
       haoKaProductMapper.insert(dbHaoKaProduct);
       // 测试 operator 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setOperator(null)));
       // 测试 sku 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setSku(null)));
       // 测试 name 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setName(null)));
       // 测试 haokaProductTypeId 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setHaokaProductTypeId(null)));
       // 测试 belongAreaCode 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setBelongAreaCode(null)));
       // 测试 haokaProductChannelId 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setHaokaProductChannelId(null)));
       // 测试 haokaProductLimitId 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setHaokaProductLimitId(null)));
       // 测试 idCardNumVerify 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setIdCardNumVerify(null)));
       // 测试 idCardImgVerify 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setIdCardImgVerify(null)));
       // 测试 produceAddress 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setProduceAddress(null)));
       // 测试 needBlacklistFilter 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setNeedBlacklistFilter(null)));
       // 测试 enableStockLimit 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setEnableStockLimit(null)));
       // 测试 stockNum 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setStockNum(null)));
       // 测试 stockWarnNum 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setStockWarnNum(null)));
       // 测试 produceRemarks 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setProduceRemarks(null)));
       // 测试 settlementRules 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setSettlementRules(null)));
       // 测试 estimatedRevenue 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setEstimatedRevenue(null)));
       // 测试 onSale 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setOnSale(null)));
       // 测试 isTop 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setIsTop(null)));
       // 测试 createTime 不匹配
       haoKaProductMapper.insert(cloneIgnoreId(dbHaoKaProduct, o -> o.setCreateTime(null)));
       // 准备参数
       HaoKaProductPageReqVO reqVO = new HaoKaProductPageReqVO();
       reqVO.setOperator(null);
       reqVO.setSku(null);
       reqVO.setName(null);
       reqVO.setHaokaProductTypeId(null);
       reqVO.setBelongAreaCode(null);
       reqVO.setHaokaProductChannelId(null);
       reqVO.setHaokaProductLimitId(null);
       reqVO.setIdCardNumVerify(null);
       reqVO.setIdCardImgVerify(null);
       reqVO.setProduceAddress(null);
       reqVO.setNeedBlacklistFilter(null);
       reqVO.setEnableStockLimit(null);
       reqVO.setStockNum(null);
       reqVO.setStockWarnNum(null);
       reqVO.setProduceRemarks(null);
       reqVO.setSettlementRules(null);
       reqVO.setEstimatedRevenue(null);
       reqVO.setOnSale(null);
       reqVO.setIsTop(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<HaoKaProductDO> pageResult = haoKaProductService.getHaoKaProductPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbHaoKaProduct, pageResult.getList().get(0));
    }

}