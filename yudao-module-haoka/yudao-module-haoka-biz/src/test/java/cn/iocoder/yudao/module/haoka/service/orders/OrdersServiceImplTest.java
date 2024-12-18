package cn.iocoder.yudao.module.haoka.service.orders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.orders.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.orders.OrdersMapper;
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
 * {@link OrdersServiceImpl} 的单元测试类
 *
 * @author xiongxiong
 */
@Import(OrdersServiceImpl.class)
public class OrdersServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OrdersServiceImpl ordersService;

    @Resource
    private OrdersMapper ordersMapper;

    @Test
    public void testCreateOrders_success() {
        // 准备参数
        OrdersSaveReqVO createReqVO = randomPojo(OrdersSaveReqVO.class).setId(null);

        // 调用
        Long ordersId = ordersService.createOrders(createReqVO);
        // 断言
        assertNotNull(ordersId);
        // 校验记录的属性是否正确
        OrdersDO orders = ordersMapper.selectById(ordersId);
        assertPojoEquals(createReqVO, orders, "id");
    }

    @Test
    public void testUpdateOrders_success() {
        // mock 数据
        OrdersDO dbOrders = randomPojo(OrdersDO.class);
        ordersMapper.insert(dbOrders);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OrdersSaveReqVO updateReqVO = randomPojo(OrdersSaveReqVO.class, o -> {
            o.setId(dbOrders.getId()); // 设置更新的 ID
        });

        // 调用
        ordersService.updateOrders(updateReqVO);
        // 校验是否更新正确
        OrdersDO orders = ordersMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, orders);
    }

    @Test
    public void testUpdateOrders_notExists() {
        // 准备参数
        OrdersSaveReqVO updateReqVO = randomPojo(OrdersSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> ordersService.updateOrders(updateReqVO), ORDERS_NOT_EXISTS);
    }

    @Test
    public void testDeleteOrders_success() {
        // mock 数据
        OrdersDO dbOrders = randomPojo(OrdersDO.class);
        ordersMapper.insert(dbOrders);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOrders.getId();

        // 调用
        ordersService.deleteOrders(id);
       // 校验数据不存在了
       assertNull(ordersMapper.selectById(id));
    }

    @Test
    public void testDeleteOrders_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> ordersService.deleteOrders(id), ORDERS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOrdersPage() {
       // mock 数据
       OrdersDO dbOrders = randomPojo(OrdersDO.class, o -> { // 等会查询到
           o.setSupplierProductName(null);
           o.setSupplierProductSku(null);
           o.setSourceId(null);
           o.setProductSku(null);
           o.setSourceSku(null);
           o.setIdCardNum(null);
           o.setAddressMobile(null);
           o.setTrackingNumber(null);
           o.setStatus(null);
           o.setFlag(null);
           o.setSource(null);
           o.setOrderedAt(null);
           o.setProducedAt(null);
           o.setDeliveredAt(null);
           o.setActivatedAt(null);
           o.setRechargedAt(null);
           o.setStatusUpdatedAt(null);
           o.setRefundStatus(null);
           o.setActiveStatus(null);
           o.setIccid(null);
           o.setRealSourceId(null);
           o.setMerchantName(null);
           o.setUpStatus(null);
           o.setUpstreamOrderId(null);
           o.setStatusName(null);
       });
       ordersMapper.insert(dbOrders);
       // 测试 supplierProductName 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setSupplierProductName(null)));
       // 测试 supplierProductSku 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setSupplierProductSku(null)));
       // 测试 sourceId 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setSourceId(null)));
       // 测试 productSku 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setProductSku(null)));
       // 测试 sourceSku 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setSourceSku(null)));
       // 测试 idCardNum 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setIdCardNum(null)));
       // 测试 addressMobile 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setAddressMobile(null)));
       // 测试 trackingNumber 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setTrackingNumber(null)));
       // 测试 status 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setStatus(null)));
       // 测试 flag 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setFlag(null)));
       // 测试 source 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setSource(null)));
       // 测试 orderedAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setOrderedAt(null)));
       // 测试 producedAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setProducedAt(null)));
       // 测试 deliveredAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setDeliveredAt(null)));
       // 测试 activatedAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setActivatedAt(null)));
       // 测试 rechargedAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setRechargedAt(null)));
       // 测试 statusUpdatedAt 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setStatusUpdatedAt(null)));
       // 测试 refundStatus 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setRefundStatus(null)));
       // 测试 activeStatus 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setActiveStatus(null)));
       // 测试 iccid 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setIccid(null)));
       // 测试 realSourceId 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setRealSourceId(null)));
       // 测试 merchantName 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setMerchantName(null)));
       // 测试 upStatus 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setUpStatus(null)));
       // 测试 upstreamOrderId 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setUpstreamOrderId(null)));
       // 测试 statusName 不匹配
       ordersMapper.insert(cloneIgnoreId(dbOrders, o -> o.setStatusName(null)));
       // 准备参数
       OrdersPageReqVO reqVO = new OrdersPageReqVO();
       reqVO.setSupplierProductName(null);
       reqVO.setSupplierProductSku(null);
       reqVO.setSourceId(null);
       reqVO.setProductSku(null);
       reqVO.setSourceSku(null);
       reqVO.setIdCardNum(null);
       reqVO.setAddressMobile(null);
       reqVO.setTrackingNumber(null);
       reqVO.setStatus(null);
       reqVO.setFlag(null);
       reqVO.setSource(null);
       reqVO.setOrderedAt(null);
       reqVO.setProducedAt(null);
       reqVO.setDeliveredAt(null);
       reqVO.setActivatedAt(null);
       reqVO.setRechargedAt(null);
       reqVO.setStatusUpdatedAt(null);
       reqVO.setRefundStatus(null);
       reqVO.setActiveStatus(null);
       reqVO.setIccid(null);
       reqVO.setRealSourceId(null);
       reqVO.setMerchantName(null);
       reqVO.setUpStatus(null);
       reqVO.setUpstreamOrderId(null);
       reqVO.setStatusName(null);

       // 调用
       PageResult<OrdersDO> pageResult = ordersService.getOrdersPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOrders, pageResult.getList().get(0));
    }

}