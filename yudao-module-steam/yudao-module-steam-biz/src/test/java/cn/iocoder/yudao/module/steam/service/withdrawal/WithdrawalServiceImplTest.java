package cn.iocoder.yudao.module.steam.service.withdrawal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.module.steam.dal.mysql.withdrawal.WithdrawalMapper;
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
 * {@link WithdrawalServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(WithdrawalServiceImpl.class)
public class WithdrawalServiceImplTest extends BaseDbUnitTest {

    @Resource
    private WithdrawalServiceImpl withdrawalService;

    @Resource
    private WithdrawalMapper withdrawalMapper;

    @Test
    public void testCreateWithdrawal_success() {
        // 准备参数
        WithdrawalSaveReqVO createReqVO = randomPojo(WithdrawalSaveReqVO.class).setId(null);

        // 调用
        Long withdrawalId = withdrawalService.createWithdrawal(createReqVO);
        // 断言
        assertNotNull(withdrawalId);
        // 校验记录的属性是否正确
        WithdrawalDO withdrawal = withdrawalMapper.selectById(withdrawalId);
        assertPojoEquals(createReqVO, withdrawal, "id");
    }

    @Test
    public void testUpdateWithdrawal_success() {
        // mock 数据
        WithdrawalDO dbWithdrawal = randomPojo(WithdrawalDO.class);
        withdrawalMapper.insert(dbWithdrawal);// @Sql: 先插入出一条存在的数据
        // 准备参数
        WithdrawalSaveReqVO updateReqVO = randomPojo(WithdrawalSaveReqVO.class, o -> {
            o.setId(dbWithdrawal.getId()); // 设置更新的 ID
        });

        // 调用
        withdrawalService.updateWithdrawal(updateReqVO);
        // 校验是否更新正确
        WithdrawalDO withdrawal = withdrawalMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, withdrawal);
    }

    @Test
    public void testUpdateWithdrawal_notExists() {
        // 准备参数
        WithdrawalSaveReqVO updateReqVO = randomPojo(WithdrawalSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> withdrawalService.updateWithdrawal(updateReqVO), WITHDRAWAL_NOT_EXISTS);
    }

    @Test
    public void testDeleteWithdrawal_success() {
        // mock 数据
        WithdrawalDO dbWithdrawal = randomPojo(WithdrawalDO.class);
        withdrawalMapper.insert(dbWithdrawal);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbWithdrawal.getId();

        // 调用
        withdrawalService.deleteWithdrawal(id);
       // 校验数据不存在了
       assertNull(withdrawalMapper.selectById(id));
    }

    @Test
    public void testDeleteWithdrawal_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> withdrawalService.deleteWithdrawal(id), WITHDRAWAL_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetWithdrawalPage() {
       // mock 数据
       WithdrawalDO dbWithdrawal = randomPojo(WithdrawalDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setUserType(null);
           o.setPayOrderId(null);
           o.setPayChannelCode(null);
           o.setPayTime(null);
           o.setPayRefundId(null);
           o.setRefundPrice(null);
           o.setRefundTime(null);
           o.setCreateTime(null);
           o.setPayStatus(null);
           o.setWithdrawalPrice(null);
           o.setWithdrawalInfo(null);
           o.setServiceFee(null);
           o.setServiceFeeRate(null);
           o.setPaymentAmount(null);
       });
       withdrawalMapper.insert(dbWithdrawal);
       // 测试 userId 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setUserId(null)));
       // 测试 userType 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setUserType(null)));
       // 测试 payOrderId 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPayOrderId(null)));
       // 测试 payChannelCode 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPayChannelCode(null)));
       // 测试 payTime 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPayTime(null)));
       // 测试 payRefundId 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPayRefundId(null)));
       // 测试 refundPrice 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setRefundPrice(null)));
       // 测试 refundTime 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setRefundTime(null)));
       // 测试 createTime 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setCreateTime(null)));
       // 测试 payStatus 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPayStatus(null)));
       // 测试 withdrawalPrice 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setWithdrawalPrice(null)));
       // 测试 withdrawalInfo 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setWithdrawalInfo(null)));
       // 测试 serviceFee 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setServiceFee(null)));
       // 测试 serviceFeeRate 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setServiceFeeRate(null)));
       // 测试 paymentAmount 不匹配
       withdrawalMapper.insert(cloneIgnoreId(dbWithdrawal, o -> o.setPaymentAmount(null)));
       // 准备参数
       WithdrawalPageReqVO reqVO = new WithdrawalPageReqVO();
       reqVO.setUserId(null);
       reqVO.setUserType(null);
       reqVO.setPayOrderId(null);
       reqVO.setPayChannelCode(null);
       reqVO.setPayTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPayRefundId(null);
       reqVO.setRefundPrice(null);
       reqVO.setRefundTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPayStatus(null);
       reqVO.setWithdrawalPrice(null);
       reqVO.setWithdrawalInfo(null);
       reqVO.setServiceFee(null);
       reqVO.setServiceFeeRate(null);
       reqVO.setPaymentAmount(null);

       // 调用
       PageResult<WithdrawalDO> pageResult = withdrawalService.getWithdrawalPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbWithdrawal, pageResult.getList().get(0));
    }

}