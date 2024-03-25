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
           o.setCreateTime(null);
           o.setBuyUserId(null);
           o.setBuyUserType(null);
           o.setBuyBindUserId(null);
           o.setBuySteamId(null);
           o.setBuyTradeLinks(null);
           o.setSellUserId(null);
           o.setSellUserType(null);
           o.setSellBindUserId(null);
           o.setSellSteamId(null);
           o.setOrderNo(null);
           o.setMerchantNo(null);
           o.setCommodityTemplateId(null);
           o.setRealCommodityId(null);
           o.setCommodityId(null);
           o.setFastShipping(null);
           o.setPayOrderId(null);
           o.setPayChannelCode(null);
           o.setPayTime(null);
           o.setPayAmount(null);
           o.setPayStatus(null);
           o.setPayOrderStatus(null);
           o.setPayRefundId(null);
           o.setRefundPrice(null);
           o.setRefundTime(null);
           o.setServiceFeeUserType(null);
           o.setServiceFee(null);
           o.setMerchantOrderNo(null);
           o.setCommodityHashName(null);
           o.setTransferDamagesRet(null);
           o.setSellCashStatus(null);
           o.setTransferStatus(null);
           o.setTransferRefundAmount(null);
           o.setServiceFeeRate(null);
           o.setServiceFeeUserId(null);
           o.setTransferDamagesTime(null);
           o.setMarketName(null);
           o.setTransferText(null);
           o.setPurchasePrice(null);
           o.setServiceFeeRet(null);
           o.setShippingMode(null);
           o.setUuMerchantOrderNo(null);
           o.setUuBuyerUserId(null);
           o.setUuFailCode(null);
           o.setUuFailReason(null);
           o.setUuOrderNo(null);
           o.setUuNotifyDesc(null);
           o.setUuNotifyType(null);
           o.setUuOrderStatus(null);
           o.setUuOrderSubStatus(null);
           o.setUuOrderSubType(null);
           o.setUuOrderType(null);
           o.setUuShippingMode(null);
           o.setUuTradeOfferId(null);
           o.setUuTradeOfferLinks(null);
       });
       youyouOrderMapper.insert(dbYouyouOrder);
       // 测试 createTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCreateTime(null)));
       // 测试 buyUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setBuyUserId(null)));
       // 测试 buyUserType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setBuyUserType(null)));
       // 测试 buyBindUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setBuyBindUserId(null)));
       // 测试 buySteamId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setBuySteamId(null)));
       // 测试 buyTradeLinks 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setBuyTradeLinks(null)));
       // 测试 sellUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellUserId(null)));
       // 测试 sellUserType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellUserType(null)));
       // 测试 sellBindUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellBindUserId(null)));
       // 测试 sellSteamId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellSteamId(null)));
       // 测试 orderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setOrderNo(null)));
       // 测试 merchantNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setMerchantNo(null)));
       // 测试 commodityTemplateId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityTemplateId(null)));
       // 测试 realCommodityId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRealCommodityId(null)));
       // 测试 commodityId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityId(null)));
       // 测试 fastShipping 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setFastShipping(null)));
       // 测试 payOrderId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayOrderId(null)));
       // 测试 payChannelCode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayChannelCode(null)));
       // 测试 payTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayTime(null)));
       // 测试 payAmount 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayAmount(null)));
       // 测试 payStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayStatus(null)));
       // 测试 payOrderStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayOrderStatus(null)));
       // 测试 payRefundId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPayRefundId(null)));
       // 测试 refundPrice 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRefundPrice(null)));
       // 测试 refundTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setRefundTime(null)));
       // 测试 serviceFeeUserType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setServiceFeeUserType(null)));
       // 测试 serviceFee 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setServiceFee(null)));
       // 测试 merchantOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setMerchantOrderNo(null)));
       // 测试 commodityHashName 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setCommodityHashName(null)));
       // 测试 transferDamagesRet 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferDamagesRet(null)));
       // 测试 sellCashStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setSellCashStatus(null)));
       // 测试 transferStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferStatus(null)));
       // 测试 transferRefundAmount 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferRefundAmount(null)));
       // 测试 serviceFeeRate 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setServiceFeeRate(null)));
       // 测试 serviceFeeUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setServiceFeeUserId(null)));
       // 测试 transferDamagesTime 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferDamagesTime(null)));
       // 测试 marketName 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setMarketName(null)));
       // 测试 transferText 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setTransferText(null)));
       // 测试 purchasePrice 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setPurchasePrice(null)));
       // 测试 serviceFeeRet 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setServiceFeeRet(null)));
       // 测试 shippingMode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setShippingMode(null)));
       // 测试 uuMerchantOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuMerchantOrderNo(null)));
       // 测试 uuBuyerUserId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuBuyerUserId(null)));
       // 测试 uuFailCode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuFailCode(null)));
       // 测试 uuFailReason 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuFailReason(null)));
       // 测试 uuOrderNo 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderNo(null)));
       // 测试 uuNotifyDesc 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuNotifyDesc(null)));
       // 测试 uuNotifyType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuNotifyType(null)));
       // 测试 uuOrderStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderStatus(null)));
       // 测试 uuOrderSubStatus 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderSubStatus(null)));
       // 测试 uuOrderSubType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderSubType(null)));
       // 测试 uuOrderType 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuOrderType(null)));
       // 测试 uuShippingMode 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuShippingMode(null)));
       // 测试 uuTradeOfferId 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuTradeOfferId(null)));
       // 测试 uuTradeOfferLinks 不匹配
       youyouOrderMapper.insert(cloneIgnoreId(dbYouyouOrder, o -> o.setUuTradeOfferLinks(null)));
       // 准备参数
       YouyouOrderPageReqVO reqVO = new YouyouOrderPageReqVO();
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setBuyUserId(null);
       reqVO.setBuyUserType(null);
       reqVO.setBuyBindUserId(null);
       reqVO.setBuySteamId(null);
       reqVO.setBuyTradeLinks(null);
       reqVO.setSellUserId(null);
       reqVO.setSellUserType(null);
       reqVO.setSellBindUserId(null);
       reqVO.setSellSteamId(null);
       reqVO.setOrderNo(null);
       reqVO.setMerchantNo(null);
       reqVO.setCommodityTemplateId(null);
       reqVO.setRealCommodityId(null);
       reqVO.setCommodityId(null);
       reqVO.setFastShipping(null);
       reqVO.setPayOrderId(null);
       reqVO.setPayChannelCode(null);
       reqVO.setPayTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPayAmount(null);
       reqVO.setPayStatus(null);
       reqVO.setPayOrderStatus(null);
       reqVO.setPayRefundId(null);
       reqVO.setRefundPrice(null);
       reqVO.setRefundTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setServiceFeeUserType(null);
       reqVO.setServiceFee(null);
       reqVO.setMerchantOrderNo(null);
       reqVO.setCommodityHashName(null);
       reqVO.setTransferDamagesRet(null);
       reqVO.setSellCashStatus(null);
       reqVO.setTransferStatus(null);
       reqVO.setTransferRefundAmount(null);
       reqVO.setServiceFeeRate(null);
       reqVO.setServiceFeeUserId(null);
       reqVO.setTransferDamagesTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setMarketName(null);
       reqVO.setTransferText(null);
       reqVO.setPurchasePrice(null);
       reqVO.setServiceFeeRet(null);
       reqVO.setShippingMode(null);
       reqVO.setUuMerchantOrderNo(null);
       reqVO.setUuBuyerUserId(null);
       reqVO.setUuFailCode(null);
       reqVO.setUuFailReason(null);
       reqVO.setUuOrderNo(null);
       reqVO.setUuNotifyDesc(null);
       reqVO.setUuNotifyType(null);
       reqVO.setUuOrderStatus(null);
       reqVO.setUuOrderSubStatus(null);
       reqVO.setUuOrderSubType(null);
       reqVO.setUuOrderType(null);
       reqVO.setUuShippingMode(null);
       reqVO.setUuTradeOfferId(null);
       reqVO.setUuTradeOfferLinks(null);

       // 调用
       PageResult<YouyouOrderDO> pageResult = youyouOrderService.getYouyouOrderPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouOrder, pageResult.getList().get(0));
    }

}