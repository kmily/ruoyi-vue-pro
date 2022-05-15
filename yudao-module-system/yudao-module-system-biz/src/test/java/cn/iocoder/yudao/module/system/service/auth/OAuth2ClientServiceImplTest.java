package cn.iocoder.yudao.module.system.service.auth;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client.OAuth2ClientCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client.OAuth2ClientUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.auth.OAuth2ClientDO;
import cn.iocoder.yudao.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import cn.iocoder.yudao.module.system.mq.producer.auth.OAuth2ClientProducer;
import cn.iocoder.yudao.module.system.service.oauth2.OAuth2ClientServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.max;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.OAUTH2_CLIENT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
* {@link OAuth2ClientServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(OAuth2ClientServiceImpl.class)
public class OAuth2ClientServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OAuth2ClientServiceImpl oauth2ClientService;

    @Resource
    private OAuth2ClientMapper oauth2ClientMapper;

    @MockBean
    private OAuth2ClientProducer oauth2ClientProducer;

    @Test
    public void testInitLocalCache() {
        // mock 数据
        OAuth2ClientDO clientDO1 = randomPojo(OAuth2ClientDO.class);
        oauth2ClientMapper.insert(clientDO1);
        OAuth2ClientDO clientDO2 = randomPojo(OAuth2ClientDO.class);
        oauth2ClientMapper.insert(clientDO2);

        // 调用
        oauth2ClientService.initLocalCache();
        // 断言 clientCache 缓存
        Map<String, OAuth2ClientDO> clientCache = oauth2ClientService.getClientCache();
        assertEquals(2, clientCache.size());
        assertPojoEquals(clientDO1, clientCache.get(clientDO1.getClientId()));
        assertPojoEquals(clientDO2, clientCache.get(clientDO2.getClientId()));
        // 断言 maxUpdateTime 缓存
        assertEquals(max(clientDO1.getUpdateTime(), clientDO2.getUpdateTime()), oauth2ClientService.getMaxUpdateTime());
    }

    @Test
    public void testCreateOAuth2Client_success() {
        // 准备参数
        OAuth2ClientCreateReqVO reqVO = randomPojo(OAuth2ClientCreateReqVO.class,
                o -> o.setLogo(randomString()));

        // 调用
        Long oauth2ClientId = oauth2ClientService.createOAuth2Client(reqVO);
        // 断言
        assertNotNull(oauth2ClientId);
        // 校验记录的属性是否正确
        OAuth2ClientDO oAuth2Client = oauth2ClientMapper.selectById(oauth2ClientId);
        assertPojoEquals(reqVO, oAuth2Client);
        verify(oauth2ClientProducer).sendOAuth2ClientRefreshMessage();
    }

    @Test
    public void testUpdateOAuth2Client_success() {
        // mock 数据
        OAuth2ClientDO dbOAuth2Client = randomPojo(OAuth2ClientDO.class);
        oauth2ClientMapper.insert(dbOAuth2Client);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OAuth2ClientUpdateReqVO reqVO = randomPojo(OAuth2ClientUpdateReqVO.class, o -> {
            o.setId(dbOAuth2Client.getId()); // 设置更新的 ID
            o.setLogo(randomString());
        });

        // 调用
        oauth2ClientService.updateOAuth2Client(reqVO);
        // 校验是否更新正确
        OAuth2ClientDO oAuth2Client = oauth2ClientMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, oAuth2Client);
        verify(oauth2ClientProducer).sendOAuth2ClientRefreshMessage();
    }

    @Test
    public void testUpdateOAuth2Client_notExists() {
        // 准备参数
        OAuth2ClientUpdateReqVO reqVO = randomPojo(OAuth2ClientUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> oauth2ClientService.updateOAuth2Client(reqVO), OAUTH2_CLIENT_NOT_EXISTS);
    }

    @Test
    public void testDeleteOAuth2Client_success() {
        // mock 数据
        OAuth2ClientDO dbOAuth2Client = randomPojo(OAuth2ClientDO.class);
        oauth2ClientMapper.insert(dbOAuth2Client);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOAuth2Client.getId();

        // 调用
        oauth2ClientService.deleteOAuth2Client(id);
        // 校验数据不存在了
        assertNull(oauth2ClientMapper.selectById(id));
        verify(oauth2ClientProducer).sendOAuth2ClientRefreshMessage();
    }

    @Test
    public void testDeleteOAuth2Client_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> oauth2ClientService.deleteOAuth2Client(id), OAUTH2_CLIENT_NOT_EXISTS);
    }

    @Test
    @Disabled
    public void testGetOAuth2ClientPage() {
       // mock 数据
       OAuth2ClientDO dbOAuth2Client = randomPojo(OAuth2ClientDO.class, o -> { // 等会查询到
           o.setName("潜龙");
           o.setStatus(CommonStatusEnum.ENABLE.getStatus());
       });
       oauth2ClientMapper.insert(dbOAuth2Client);
       // 测试 name 不匹配
       oauth2ClientMapper.insert(cloneIgnoreId(dbOAuth2Client, o -> o.setName("凤凰")));
       // 测试 status 不匹配
       oauth2ClientMapper.insert(cloneIgnoreId(dbOAuth2Client, o -> o.setStatus(CommonStatusEnum.ENABLE.getStatus())));
       // 准备参数
       OAuth2ClientPageReqVO reqVO = new OAuth2ClientPageReqVO();
       reqVO.setName("long");
       reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());

       // 调用
       PageResult<OAuth2ClientDO> pageResult = oauth2ClientService.getOAuth2ClientPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOAuth2Client, pageResult.getList().get(0));
    }

}
