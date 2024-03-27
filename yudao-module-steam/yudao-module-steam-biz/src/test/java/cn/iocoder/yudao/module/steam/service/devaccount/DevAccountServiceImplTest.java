package cn.iocoder.yudao.module.steam.service.devaccount;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.mysql.devaccount.DevAccountMapper;
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
 * {@link DevAccountServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(DevAccountServiceImpl.class)
public class DevAccountServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DevAccountServiceImpl devAccountService;

    @Resource
    private DevAccountMapper devAccountMapper;

    @Test
    public void testCreateDevAccount_success() {
        // 准备参数
        DevAccountSaveReqVO createReqVO = randomPojo(DevAccountSaveReqVO.class).setId(null);

        // 调用
        Long devAccountId = devAccountService.createDevAccount(createReqVO);
        // 断言
        assertNotNull(devAccountId);
        // 校验记录的属性是否正确
        DevAccountDO devAccount = devAccountMapper.selectById(devAccountId);
        assertPojoEquals(createReqVO, devAccount, "id");
    }

    @Test
    public void testUpdateDevAccount_success() {
        // mock 数据
        DevAccountDO dbDevAccount = randomPojo(DevAccountDO.class);
        devAccountMapper.insert(dbDevAccount);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DevAccountSaveReqVO updateReqVO = randomPojo(DevAccountSaveReqVO.class, o -> {
            o.setId(dbDevAccount.getId()); // 设置更新的 ID
        });

        // 调用
        devAccountService.updateDevAccount(updateReqVO);
        // 校验是否更新正确
        DevAccountDO devAccount = devAccountMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, devAccount);
    }

    @Test
    public void testUpdateDevAccount_notExists() {
        // 准备参数
        DevAccountSaveReqVO updateReqVO = randomPojo(DevAccountSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> devAccountService.updateDevAccount(updateReqVO), DEV_ACCOUNT_NOT_EXISTS);
    }

    @Test
    public void testDeleteDevAccount_success() {
        // mock 数据
        DevAccountDO dbDevAccount = randomPojo(DevAccountDO.class);
        devAccountMapper.insert(dbDevAccount);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDevAccount.getId();

        // 调用
        devAccountService.deleteDevAccount(id);
       // 校验数据不存在了
       assertNull(devAccountMapper.selectById(id));
    }

    @Test
    public void testDeleteDevAccount_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> devAccountService.deleteDevAccount(id), DEV_ACCOUNT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDevAccountPage() {
       // mock 数据
       DevAccountDO dbDevAccount = randomPojo(DevAccountDO.class, o -> { // 等会查询到
           o.setUserName(null);
           o.setCallbackUrl(null);
           o.setStatus(null);
           o.setUserType(null);
       });
       devAccountMapper.insert(dbDevAccount);
       // 测试 userName 不匹配
       devAccountMapper.insert(cloneIgnoreId(dbDevAccount, o -> o.setUserName(null)));
       // 测试 steamId 不匹配
       devAccountMapper.insert(cloneIgnoreId(dbDevAccount, o -> o.setCallbackUrl(null)));
       // 测试 status 不匹配
       devAccountMapper.insert(cloneIgnoreId(dbDevAccount, o -> o.setStatus(null)));
       // 测试 userType 不匹配
       devAccountMapper.insert(cloneIgnoreId(dbDevAccount, o -> o.setUserType(null)));
       // 准备参数
       DevAccountPageReqVO reqVO = new DevAccountPageReqVO();
       reqVO.setUserName(null);
       reqVO.setCallbackUrl(null);
       reqVO.setStatus(null);
       reqVO.setUserType(null);

       // 调用
       PageResult<DevAccountDO> pageResult = devAccountService.getDevAccountPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDevAccount, pageResult.getList().get(0));
    }

}