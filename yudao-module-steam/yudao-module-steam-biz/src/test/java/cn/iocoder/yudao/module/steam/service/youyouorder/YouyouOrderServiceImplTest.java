package cn.iocoder.yudao.module.steam.service.youyouorder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link YouyouOrderServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouOrderServiceImpl.class)
public class YouyouOrderServiceImplTest extends BaseDbUnitTest {

    @Resource
    private YouyouOrderServiceImpl youyouOrderService;

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Test
    public void testCreateYouyouOrder_success() {
        // 准备参数
        YouyouOrderSaveReqVO createReqVO = randomPojo(YouyouOrderSaveReqVO.class).setId(null);

        // 调用
        Long youyouOrderId = youyouOrderService.createYouyouOrder(createReqVO);
        // 断言
        assertNotNull(youyouOrderId);
        // 校验记录的属性是否正确
        YouyouOrderDO youyouOrder = youyouOrderMapper.selectById(youyouOrderId);
        assertPojoEquals(createReqVO, youyouOrder, "id");
    }

    @Test
    public void testUpdateYouyouOrder_success() {
        // mock 数据
        YouyouOrderDO dbYouyouOrder = randomPojo(YouyouOrderDO.class);
        youyouOrderMapper.insert(dbYouyouOrder);// @Sql: 先插入出一条存在的数据
        // 准备参数
        YouyouOrderSaveReqVO updateReqVO = randomPojo(YouyouOrderSaveReqVO.class, o -> {
            o.setId(dbYouyouOrder.getId()); // 设置更新的 ID
        });

        // 调用
        youyouOrderService.updateYouyouOrder(updateReqVO);
        // 校验是否更新正确
        YouyouOrderDO youyouOrder = youyouOrderMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, youyouOrder);
    }

    @Test
    public void testUpdateYouyouOrder_notExists() {
        // 准备参数
        YouyouOrderSaveReqVO updateReqVO = randomPojo(YouyouOrderSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> youyouOrderService.updateYouyouOrder(updateReqVO), YOUYOU_ORDER_NOT_EXISTS);
    }

    @Test
    public void testDeleteYouyouOrder_success() {
        // mock 数据
        YouyouOrderDO dbYouyouOrder = randomPojo(YouyouOrderDO.class);
        youyouOrderMapper.insert(dbYouyouOrder);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbYouyouOrder.getId();

        // 调用
        youyouOrderService.deleteYouyouOrder(id);
       // 校验数据不存在了
       assertNull(youyouOrderMapper.selectById(id));
    }

    @Test
    public void testDeleteYouyouOrder_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> youyouOrderService.deleteYouyouOrder(id), YOUYOU_ORDER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetYouyouOrderPage() {
       // mock 数据
       YouyouOrderDO dbYouyouOrder = randomPojo(YouyouOrderDO.class, o -> { // 等会查询到
           o.setPayStatus(null);
           o.setPayOrderId(null);
           o.setPayChannelCode(null);
           o.setPayTime(null);
           o.setRefundPrice(null);
           o.setPayRefundId(null);
           o.setRefundTime(null);
           o.setCreateTime(null);
           o.setUserType(null);
           o.setTransferText(null);
           o.setTransferStatus(null);
           o.setMerchantOrderNo(null);
           o.setOrderNo(null);
           o.setShippingMode(null);
           o.setCommodityTemplateId(null);
           o.setCommodityHashName(null);
           o.setCommodityId(null);
           o.setPurchasePrice(null);
           o.setRealCommodityId(null);
           o.setUuOrderNo(null);
           o.setUuMerchantOrderNo(null);
           o.setUuOrderStatus(null);
           o.setSellCashStatus(null);
           o.setSellUserId(null);
           o.setSellUserType(null);
       });
       youyouOrderMapper.insert(dbYouyouOrder);
       // 测试 payStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayStatus(null)));
       // 测试 payOrderId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayOrderId(null)));
       // 测试 payChannelCode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayChannelCode(null)));
       // 测试 payTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayTime(null)));
       // 测试 refundPrice 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRefundPrice(null)));
       // 测试 payRefundId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayRefundId(null)));
       // 测试 refundTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRefundTime(null)));
       // 测试 createTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCreateTime(null)));
       // 测试 userType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUserType(null)));
       // 测试 transferText 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferText(null)));
       // 测试 transferStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferStatus(null)));
       // 测试 merchantOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setMerchantOrderNo(null)));
       // 测试 orderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setOrderNo(null)));
       // 测试 shippingMode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setShippingMode(null)));
       // 测试 commodityTemplateId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityTemplateId(null)));
       // 测试 commodityHashName 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityHashName(null)));
       // 测试 commodityId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityId(null)));
       // 测试 purchasePrice 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPurchasePrice(null)));
       // 测试 realCommodityId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRealCommodityId(null)));
       // 测试 uuOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderNo(null)));
       // 测试 uuMerchantOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuMerchantOrderNo(null)));
       // 测试 uuOrderStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderStatus(null)));
       // 测试 sellCashStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellCashStatus(null)));
       // 测试 sellUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellUserId(null)));
       // 测试 sellUserType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellUserType(null)));
       // 准备参数
       YouyouOrderPageReqVO reqVO = new YouyouOrderPageReqVO();
       reqVO.setPayStatus(null);
       reqVO.setPayOrderId(null);
       reqVO.setPayChannelCode(null);
       reqVO.setPayTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setRefundPrice(null);
       reqVO.setPayRefundId(null);
       reqVO.setRefundTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setUserType(null);
       reqVO.setTransferText(null);
       reqVO.setTransferStatus(null);
       reqVO.setMerchantOrderNo(null);
       reqVO.setOrderNo(null);
       reqVO.setShippingMode(null);
       reqVO.setCommodityTemplateId(null);
       reqVO.setCommodityHashName(null);
       reqVO.setCommodityId(null);
       reqVO.setPurchasePrice(null);
       reqVO.setRealCommodityId(null);
       reqVO.setUuOrderNo(null);
       reqVO.setUuMerchantOrderNo(null);
       reqVO.setUuOrderStatus(null);
       reqVO.setSellCashStatus(null);
       reqVO.setSellUserId(null);
       reqVO.setSellUserType(null);

       // 调用
       PageResult<YouyouOrderDO> pageResult = youyouOrderService.getYouyouOrderPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouOrder, pageResult.getList().get(0));
    }

}