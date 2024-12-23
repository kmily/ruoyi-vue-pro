package cn.iocoder.yudao.module.haoka.service.ordersource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.ordersource.OrderSourceMapper;
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
 * {@link OrderSourceServiceImpl} 的单元测试类
 *
 * @author xiongxiong
 */
@Import(OrderSourceServiceImpl.class)
public class OrderSourceServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OrderSourceServiceImpl orderSourceService;

    @Resource
    private OrderSourceMapper orderSourceMapper;

    @Test
    public void testCreateOrderSource_success() {
        // 准备参数
        OrderSourceSaveReqVO createReqVO = randomPojo(OrderSourceSaveReqVO.class).setId(null);

        // 调用
        Long orderSourceId = orderSourceService.createOrderSource(createReqVO);
        // 断言
        assertNotNull(orderSourceId);
        // 校验记录的属性是否正确
        OrderSourceDO orderSource = orderSourceMapper.selectById(orderSourceId);
        assertPojoEquals(createReqVO, orderSource, "id");
    }

    @Test
    public void testUpdateOrderSource_success() {
        // mock 数据
        OrderSourceDO dbOrderSource = randomPojo(OrderSourceDO.class);
        orderSourceMapper.insert(dbOrderSource);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OrderSourceSaveReqVO updateReqVO = randomPojo(OrderSourceSaveReqVO.class, o -> {
            o.setId(dbOrderSource.getId()); // 设置更新的 ID
        });

        // 调用
        orderSourceService.updateOrderSource(updateReqVO);
        // 校验是否更新正确
        OrderSourceDO orderSource = orderSourceMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, orderSource);
    }

    @Test
    public void testUpdateOrderSource_notExists() {
        // 准备参数
        OrderSourceSaveReqVO updateReqVO = randomPojo(OrderSourceSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> orderSourceService.updateOrderSource(updateReqVO), ORDER_SOURCE_NOT_EXISTS);
    }

    @Test
    public void testDeleteOrderSource_success() {
        // mock 数据
        OrderSourceDO dbOrderSource = randomPojo(OrderSourceDO.class);
        orderSourceMapper.insert(dbOrderSource);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOrderSource.getId();

        // 调用
        orderSourceService.deleteOrderSource(id);
       // 校验数据不存在了
       assertNull(orderSourceMapper.selectById(id));
    }

    @Test
    public void testDeleteOrderSource_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> orderSourceService.deleteOrderSource(id), ORDER_SOURCE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOrderSourcePage() {
       // mock 数据
       OrderSourceDO dbOrderSource = randomPojo(OrderSourceDO.class, o -> { // 等会查询到
           o.setSourceRemark(null);
           o.setChannel(null);
           o.setCreateTime(null);
       });
       orderSourceMapper.insert(dbOrderSource);
       // 测试 sourceRemark 不匹配
       orderSourceMapper.insert(cloneIgnoreId(dbOrderSource, o -> o.setSourceRemark(null)));
       // 测试 channel 不匹配
       orderSourceMapper.insert(cloneIgnoreId(dbOrderSource, o -> o.setChannel(null)));
       // 测试 createTime 不匹配
       orderSourceMapper.insert(cloneIgnoreId(dbOrderSource, o -> o.setCreateTime(null)));
       // 准备参数
       OrderSourcePageReqVO reqVO = new OrderSourcePageReqVO();
       reqVO.setSourceRemark(null);
       reqVO.setChannel(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<OrderSourceDO> pageResult = orderSourceService.getOrderSourcePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOrderSource, pageResult.getList().get(0));
    }

}