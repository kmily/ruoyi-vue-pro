package cn.iocoder.yudao.module.steam.service.invorder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
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
 * {@link InvOrderServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(InvOrderServiceImpl.class)
public class InvOrderServiceImplTest extends BaseDbUnitTest {

    @Resource
    private InvOrderServiceImpl invOrderService;

    @Resource
    private InvOrderMapper invOrderMapper;

    @Test
    public void testCreateInvOrder_success() {
        // 准备参数
        InvOrderSaveReqVO createReqVO = randomPojo(InvOrderSaveReqVO.class).setId(null);

        // 调用
        Long invOrderId = invOrderService.createInvOrder(createReqVO);
        // 断言
        assertNotNull(invOrderId);
        // 校验记录的属性是否正确
        InvOrderDO invOrder = invOrderMapper.selectById(invOrderId);
        assertPojoEquals(createReqVO, invOrder, "id");
    }

    @Test
    public void testUpdateInvOrder_success() {
        // mock 数据
        InvOrderDO dbInvOrder = randomPojo(InvOrderDO.class);
        invOrderMapper.insert(dbInvOrder);// @Sql: 先插入出一条存在的数据
        // 准备参数
        InvOrderSaveReqVO updateReqVO = randomPojo(InvOrderSaveReqVO.class, o -> {
            o.setId(dbInvOrder.getId()); // 设置更新的 ID
        });

        // 调用
        invOrderService.updateInvOrder(updateReqVO);
        // 校验是否更新正确
        InvOrderDO invOrder = invOrderMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, invOrder);
    }

    @Test
    public void testUpdateInvOrder_notExists() {
        // 准备参数
        InvOrderSaveReqVO updateReqVO = randomPojo(InvOrderSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> invOrderService.updateInvOrder(updateReqVO), INV_ORDER_NOT_EXISTS);
    }

    @Test
    public void testDeleteInvOrder_success() {
        // mock 数据
        InvOrderDO dbInvOrder = randomPojo(InvOrderDO.class);
        invOrderMapper.insert(dbInvOrder);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbInvOrder.getId();

        // 调用
        invOrderService.deleteInvOrder(id);
       // 校验数据不存在了
       assertNull(invOrderMapper.selectById(id));
    }

    @Test
    public void testDeleteInvOrder_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> invOrderService.deleteInvOrder(id), INV_ORDER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetInvOrderPage() {
       // mock 数据
       InvOrderDO dbInvOrder = randomPojo(InvOrderDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setSteamId(null);
           o.setPayStatus(null);
           o.setPayOrderId(null);
           o.setPayChannelCode(null);
           o.setPayTime(null);
           o.setPayRefundId(null);
           o.setRefundAmount(null);
           o.setRefundTime(null);
           o.setCreateTime(null);
           o.setPaymentAmount(null);
           o.setUserType(null);
           o.setPayOrderStatus(null);
           o.setServiceFee(null);
           o.setServiceFeeRate(null);
           o.setDiscountAmount(null);
           o.setTransferText(null);
           o.setTransferStatus(null);
           o.setSellId(null);
           o.setInvDescId(null);
           o.setInvId(null);
           o.setSellUserType(null);
           o.setSellUserId(null);
           o.setSellCashStatus(null);
           o.setCommodityAmount(null);
           o.setServiceFeeUserId(null);
           o.setServiceFeeUserType(null);
           o.setServiceFeeRet(null);
           o.setPlatformName(null);
           o.setPlatformCode(null);
           o.setOrderNo(null);
           o.setMerchantNo(null);
           o.setTransferRefundAmount(null);
           o.setTransferDamagesAmount(null);
           o.setTransferDamagesTime(null);
       });
       invOrderMapper.insert(dbInvOrder);
       // 测试 userId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setUserId(null)));
       // 测试 steamId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setSteamId(null)));
       // 测试 payStatus 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayStatus(null)));
       // 测试 payOrderId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayOrderId(null)));
       // 测试 payChannelCode 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayChannelCode(null)));
       // 测试 payTime 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayTime(null)));
       // 测试 payRefundId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayRefundId(null)));
       // 测试 refundAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setRefundAmount(null)));
       // 测试 refundTime 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setRefundTime(null)));
       // 测试 createTime 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setCreateTime(null)));
       // 测试 paymentAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPaymentAmount(null)));
       // 测试 userType 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setUserType(null)));
       // 测试 payOrderStatus 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPayOrderStatus(null)));
       // 测试 serviceFee 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setServiceFee(null)));
       // 测试 serviceFeeRate 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setServiceFeeRate(null)));
       // 测试 discountAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setDiscountAmount(null)));
       // 测试 transferText 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setTransferText(null)));
       // 测试 transferStatus 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setTransferStatus(null)));
       // 测试 sellId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setSellId(null)));
       // 测试 invDescId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setInvDescId(null)));
       // 测试 invId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setInvId(null)));
       // 测试 sellUserType 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setSellUserType(null)));
       // 测试 sellUserId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setSellUserId(null)));
       // 测试 sellCashStatus 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setSellCashStatus(null)));
       // 测试 commodityAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setCommodityAmount(null)));
       // 测试 serviceFeeUserId 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setServiceFeeUserId(null)));
       // 测试 serviceFeeUserType 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setServiceFeeUserType(null)));
       // 测试 serviceFeeRet 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setServiceFeeRet(null)));
       // 测试 platformName 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPlatformName(null)));
       // 测试 platformCode 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setPlatformCode(null)));
       // 测试 orderNo 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setOrderNo(null)));
       // 测试 merchantNo 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setMerchantNo(null)));
       // 测试 transferRefundAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setTransferRefundAmount(null)));
       // 测试 transferDamagesAmount 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setTransferDamagesAmount(null)));
       // 测试 transferDamagesTime 不匹配
       invOrderMapper.insert(cloneIgnoreId(dbInvOrder, o -> o.setTransferDamagesTime(null)));
       // 准备参数
       InvOrderPageReqVO reqVO = new InvOrderPageReqVO();
       reqVO.setUserId(null);
       reqVO.setSteamId(null);
       reqVO.setPayStatus(null);
       reqVO.setPayOrderId(null);
       reqVO.setPayChannelCode(null);
       reqVO.setPayTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPayRefundId(null);
       reqVO.setRefundAmount(null);
       reqVO.setRefundTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPaymentAmount(null);
       reqVO.setUserType(null);
       reqVO.setPayOrderStatus(null);
       reqVO.setServiceFee(null);
       reqVO.setServiceFeeRate(null);
       reqVO.setDiscountAmount(null);
       reqVO.setTransferText(null);
       reqVO.setTransferStatus(null);
       reqVO.setSellId(null);
       reqVO.setInvDescId(null);
       reqVO.setInvId(null);
       reqVO.setSellUserType(null);
       reqVO.setSellUserId(null);
       reqVO.setSellCashStatus(null);
       reqVO.setCommodityAmount(null);
       reqVO.setServiceFeeUserId(null);
       reqVO.setServiceFeeUserType(null);
       reqVO.setServiceFeeRet(null);
       reqVO.setPlatformName(null);
       reqVO.setPlatformCode(null);
       reqVO.setOrderNo(null);
       reqVO.setMerchantNo(null);
       reqVO.setTransferRefundAmount(null);
       reqVO.setTransferDamagesAmount(null);
       reqVO.setTransferDamagesTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<InvOrderDO> pageResult = invOrderService.getInvOrderPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbInvOrder, pageResult.getList().get(0));
    }

}