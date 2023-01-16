package cn.iocoder.yudao.module.scan.service.users;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import cn.iocoder.yudao.module.scan.dal.mysql.users.UsersMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link UsersServiceImpl} 的单元测试类
*
* @author lyz
*/
@Import(UsersServiceImpl.class)
public class UsersServiceImplTest extends BaseDbUnitTest {

    @Resource
    private UsersServiceImpl usersService;

    @Resource
    private UsersMapper usersMapper;

    @Test
    public void testCreateUsers_success() {
        // 准备参数
        UsersCreateReqVO reqVO = randomPojo(UsersCreateReqVO.class);

        // 调用
        Long usersId = usersService.createUsers(reqVO);
        // 断言
        assertNotNull(usersId);
        // 校验记录的属性是否正确
        UsersDO users = usersMapper.selectById(usersId);
        assertPojoEquals(reqVO, users);
    }

    @Test
    public void testUpdateUsers_success() {
        // mock 数据
        UsersDO dbUsers = randomPojo(UsersDO.class);
        usersMapper.insert(dbUsers);// @Sql: 先插入出一条存在的数据
        // 准备参数
        UsersUpdateReqVO reqVO = randomPojo(UsersUpdateReqVO.class, o -> {
            o.setId(dbUsers.getId()); // 设置更新的 ID
        });

        // 调用
        usersService.updateUsers(reqVO);
        // 校验是否更新正确
        UsersDO users = usersMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, users);
    }

    @Test
    public void testUpdateUsers_notExists() {
        // 准备参数
        UsersUpdateReqVO reqVO = randomPojo(UsersUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> usersService.updateUsers(reqVO), USERS_NOT_EXISTS);
    }

    @Test
    public void testDeleteUsers_success() {
        // mock 数据
        UsersDO dbUsers = randomPojo(UsersDO.class);
        usersMapper.insert(dbUsers);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbUsers.getId();

        // 调用
        usersService.deleteUsers(id);
       // 校验数据不存在了
       assertNull(usersMapper.selectById(id));
    }

    @Test
    public void testDeleteUsers_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> usersService.deleteUsers(id), USERS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUsersPage() {
       // mock 数据
       UsersDO dbUsers = randomPojo(UsersDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setName(null);
           o.setAge(null);
           o.setSex(null);
           o.setPhone(null);
           o.setDeviceId(null);
       });
       usersMapper.insert(dbUsers);
       // 测试 createTime 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setCreateTime(null)));
       // 测试 name 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setName(null)));
       // 测试 age 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setAge(null)));
       // 测试 sex 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setSex(null)));
       // 测试 phone 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setPhone(null)));
       // 测试 deviceId 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setDeviceId(null)));
       // 准备参数
       UsersPageReqVO reqVO = new UsersPageReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setName(null);
       reqVO.setAge(null);
       reqVO.setSex(null);
       reqVO.setPhone(null);
       reqVO.setDeviceId(null);

       // 调用
       PageResult<UsersDO> pageResult = usersService.getUsersPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbUsers, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUsersList() {
       // mock 数据
       UsersDO dbUsers = randomPojo(UsersDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setName(null);
           o.setAge(null);
           o.setSex(null);
           o.setPhone(null);
           o.setDeviceId(null);
       });
       usersMapper.insert(dbUsers);
       // 测试 createTime 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setCreateTime(null)));
       // 测试 name 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setName(null)));
       // 测试 age 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setAge(null)));
       // 测试 sex 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setSex(null)));
       // 测试 phone 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setPhone(null)));
       // 测试 deviceId 不匹配
       usersMapper.insert(cloneIgnoreId(dbUsers, o -> o.setDeviceId(null)));
       // 准备参数
       UsersExportReqVO reqVO = new UsersExportReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setName(null);
       reqVO.setAge(null);
       reqVO.setSex(null);
       reqVO.setPhone(null);
       reqVO.setDeviceId(null);

       // 调用
       List<UsersDO> list = usersService.getUsersList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbUsers, list.get(0));
    }

}
