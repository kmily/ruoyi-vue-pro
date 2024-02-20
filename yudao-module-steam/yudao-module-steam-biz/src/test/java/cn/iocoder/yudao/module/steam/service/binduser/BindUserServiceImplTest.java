package cn.iocoder.yudao.module.steam.service.binduser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
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
 * {@link BindUserServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(BindUserServiceImpl.class)
public class BindUserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BindUserServiceImpl bindUserService;

    @Resource
    private BindUserMapper bindUserMapper;

    @Test
    public void testCreateBindUser_success() {
        // 准备参数
        BindUserSaveReqVO createReqVO = randomPojo(BindUserSaveReqVO.class).setId(null);

        // 调用
        Integer bindUserId = bindUserService.createBindUser(createReqVO);
        // 断言
        assertNotNull(bindUserId);
        // 校验记录的属性是否正确
        BindUserDO bindUser = bindUserMapper.selectById(bindUserId);
        assertPojoEquals(createReqVO, bindUser, "id");
    }

    @Test
    public void testUpdateBindUser_success() {
        // mock 数据
        BindUserDO dbBindUser = randomPojo(BindUserDO.class);
        bindUserMapper.insert(dbBindUser);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BindUserSaveReqVO updateReqVO = randomPojo(BindUserSaveReqVO.class, o -> {
            o.setId(dbBindUser.getId()); // 设置更新的 ID
        });

        // 调用
        bindUserService.updateBindUser(updateReqVO);
        // 校验是否更新正确
        BindUserDO bindUser = bindUserMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, bindUser);
    }

    @Test
    public void testUpdateBindUser_notExists() {
        // 准备参数
        BindUserSaveReqVO updateReqVO = randomPojo(BindUserSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> bindUserService.updateBindUser(updateReqVO), BIND_USER_NOT_EXISTS);
    }

    @Test
    public void testDeleteBindUser_success() {
        // mock 数据
        BindUserDO dbBindUser = randomPojo(BindUserDO.class);
        bindUserMapper.insert(dbBindUser);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbBindUser.getId();

        // 调用
        bindUserService.deleteBindUser(id);
       // 校验数据不存在了
       assertNull(bindUserMapper.selectById(id));
    }

    @Test
    public void testDeleteBindUser_notExists() {
        // 准备参数
        Integer id = 1;

        // 调用, 并断言异常
        assertServiceException(() -> bindUserService.deleteBindUser(id), BIND_USER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBindUserPage() {
       // mock 数据
       BindUserDO dbBindUser = randomPojo(BindUserDO.class, o -> { // 等会查询到
           o.setSteamName(null);
           o.setUserId(null);
           o.setSteamId(null);
           o.setTradeUrl(null);
           o.setApiKey(null);
           o.setRemark(null);
           o.setCreateTime(null);
           o.setMaFile(null);
       });
       bindUserMapper.insert(dbBindUser);
       // 测试 steamName 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setSteamName(null)));
       // 测试 userId 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setUserId(null)));
       // 测试 steamId 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setSteamId(null)));
       // 测试 tradeUrl 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setTradeUrl(null)));
       // 测试 apiKey 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setApiKey(null)));
       // 测试 remark 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setCreateTime(null)));
       // 测试 maFile 不匹配
       bindUserMapper.insert(cloneIgnoreId(dbBindUser, o -> o.setMaFile(null)));
       // 准备参数
       BindUserPageReqVO reqVO = new BindUserPageReqVO();
       reqVO.setSteamName(null);
       reqVO.setUserId(null);
       reqVO.setSteamId(null);
       reqVO.setTradeUrl(null);
       reqVO.setApiKey(null);
       reqVO.setRemark(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setMaFile(null);

       // 调用
       PageResult<BindUserDO> pageResult = bindUserService.getBindUserPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBindUser, pageResult.getList().get(0));
    }

}