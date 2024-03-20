package cn.iocoder.yudao.module.steam.service.alipayisv;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.alipayisv.AlipayIsvMapper;
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
 * {@link AlipayIsvServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(AlipayIsvServiceImpl.class)
public class AlipayIsvServiceImplTest extends BaseDbUnitTest {

    @Resource
    private AlipayIsvServiceImpl alipayIsvService;

    @Resource
    private AlipayIsvMapper alipayIsvMapper;

    @Test
    public void testCreateAlipayIsv_success() {
        // 准备参数
        AlipayIsvSaveReqVO createReqVO = randomPojo(AlipayIsvSaveReqVO.class).setId(null);

        // 调用
        Long alipayIsvId = alipayIsvService.createAlipayIsv(createReqVO);
        // 断言
        assertNotNull(alipayIsvId);
        // 校验记录的属性是否正确
        AlipayIsvDO alipayIsv = alipayIsvMapper.selectById(alipayIsvId);
        assertPojoEquals(createReqVO, alipayIsv, "id");
    }

    @Test
    public void testUpdateAlipayIsv_success() {
        // mock 数据
        AlipayIsvDO dbAlipayIsv = randomPojo(AlipayIsvDO.class);
        alipayIsvMapper.insert(dbAlipayIsv);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AlipayIsvSaveReqVO updateReqVO = randomPojo(AlipayIsvSaveReqVO.class, o -> {
            o.setId(dbAlipayIsv.getId()); // 设置更新的 ID
        });

        // 调用
        alipayIsvService.updateAlipayIsv(updateReqVO);
        // 校验是否更新正确
        AlipayIsvDO alipayIsv = alipayIsvMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, alipayIsv);
    }

    @Test
    public void testUpdateAlipayIsv_notExists() {
        // 准备参数
        AlipayIsvSaveReqVO updateReqVO = randomPojo(AlipayIsvSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> alipayIsvService.updateAlipayIsv(updateReqVO), ALIPAY_ISV_NOT_EXISTS);
    }

    @Test
    public void testDeleteAlipayIsv_success() {
        // mock 数据
        AlipayIsvDO dbAlipayIsv = randomPojo(AlipayIsvDO.class);
        alipayIsvMapper.insert(dbAlipayIsv);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbAlipayIsv.getId();

        // 调用
        alipayIsvService.deleteAlipayIsv(id);
       // 校验数据不存在了
       assertNull(alipayIsvMapper.selectById(id));
    }

    @Test
    public void testDeleteAlipayIsv_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> alipayIsvService.deleteAlipayIsv(id), ALIPAY_ISV_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetAlipayIsvPage() {
       // mock 数据
       AlipayIsvDO dbAlipayIsv = randomPojo(AlipayIsvDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setSystemUserId(null);
           o.setSystemUserType(null);
           o.setIsvBizId(null);
           o.setAppAuthToken(null);
           o.setAppId(null);
           o.setAuthAppId(null);
           o.setUserId(null);
           o.setMerchantPid(null);
           o.setSignStatus(null);
           o.setSignStatusList(null);
       });
       alipayIsvMapper.insert(dbAlipayIsv);
       // 测试 createTime 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setCreateTime(null)));
       // 测试 systemUserId 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setSystemUserId(null)));
       // 测试 systemUserType 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setSystemUserType(null)));
       // 测试 isvBizId 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setIsvBizId(null)));
       // 测试 appAuthToken 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setAppAuthToken(null)));
       // 测试 appId 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setAppId(null)));
       // 测试 authAppId 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setAuthAppId(null)));
       // 测试 userId 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setUserId(null)));
       // 测试 merchantPid 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setMerchantPid(null)));
       // 测试 signStatus 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setSignStatus(null)));
       // 测试 signStatusList 不匹配
       alipayIsvMapper.insert(cloneIgnoreId(dbAlipayIsv, o -> o.setSignStatusList(null)));
       // 准备参数
       AlipayIsvPageReqVO reqVO = new AlipayIsvPageReqVO();
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setSystemUserId(null);
       reqVO.setSystemUserType(null);
       reqVO.setIsvBizId(null);
       reqVO.setAppAuthToken(null);
       reqVO.setAppId(null);
       reqVO.setAuthAppId(null);
       reqVO.setUserId(null);
       reqVO.setMerchantPid(null);
       reqVO.setSignStatus(null);
       reqVO.setSignStatusList(null);

       // 调用
       PageResult<AlipayIsvDO> pageResult = alipayIsvService.getAlipayIsvPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbAlipayIsv, pageResult.getList().get(0));
    }

}