package cn.iocoder.yudao.module.member.service.MemberUser;

import cn.iocoder.yudao.module.member.service.user.MemberUserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.MemberUser.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;
import java.util.*;

import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link MemberUserServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(MemberUserServiceImpl.class)
public class MemberUserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private MemberUserServiceImpl userService;

    @Resource
    private MemberUserMapper userMapper;

    @Test
    public void testCreateUser_success() {
        // 准备参数
        MemberUserCreateReqVO reqVO = randomPojo(MemberUserCreateReqVO.class);

        // 调用
        Long userId = userService.createUser(reqVO);
        // 断言
        assertNotNull(userId);
        // 校验记录的属性是否正确
        MemberUserDO user = userMapper.selectById(userId);
        assertPojoEquals(reqVO, user);
    }

    @Test
    public void testUpdateUser_success() {
        // mock 数据
        MemberUserDO dbUser = randomPojo(MemberUserDO.class);
        userMapper.insert(dbUser);// @Sql: 先插入出一条存在的数据
        // 准备参数
        MemberUserUpdateReqVO reqVO = randomPojo(MemberUserUpdateReqVO.class, o -> {
            o.setId(dbUser.getId()); // 设置更新的 ID
        });

        // 调用
        userService.updateUser(reqVO);
        // 校验是否更新正确
        MemberUserDO user = userMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, user);
    }

    @Test
    public void testUpdateUser_notExists() {
        // 准备参数
        MemberUserUpdateReqVO reqVO = randomPojo(MemberUserUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> userService.updateUser(reqVO), USER_NOT_EXISTS);
    }

    @Test
    public void testDeleteUser_success() {
        // mock 数据
        MemberUserDO dbUser = randomPojo(MemberUserDO.class);
        userMapper.insert(dbUser);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbUser.getId();

        // 调用
        userService.deleteUser(id);
       // 校验数据不存在了
       assertNull(userMapper.selectById(id));
    }

    @Test
    public void testDeleteUser_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> userService.deleteUser(id), USER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUserPage() {
       // mock 数据
       MemberUserDO dbUser = randomPojo(MemberUserDO.class, o -> { // 等会查询到
           o.setNickname(null);
           o.setRealName(null);
           o.setAvatar(null);
           o.setStatus(null);
           o.setMobile(null);
           o.setPassword(null);
           o.setRegisterIp(null);
           o.setLoginIp(null);
           o.setPayPassword(null);
           o.setLoginDate(null);
           o.setAreaId(null);
           o.setUserGroupId(null);
           o.setPoint(null);
           o.setReferrer(null);
           o.setGender(null);
           o.setLabelId(null);
           o.setCreateTime(null);
       });
       userMapper.insert(dbUser);
       // 测试 nickname 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setNickname(null)));
       // 测试 realName 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setRealName(null)));
       // 测试 avatar 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setAvatar(null)));
       // 测试 status 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setStatus(null)));
       // 测试 mobile 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setMobile(null)));
       // 测试 password 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPassword(null)));
       // 测试 registerIp 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setRegisterIp(null)));
       // 测试 loginIp 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLoginIp(null)));
       // 测试 payPassword 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPayPassword(null)));
       // 测试 loginDate 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLoginDate(null)));
       // 测试 areaId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setAreaId(null)));
       // 测试 userGroupId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setUserGroupId(null)));
       // 测试 point 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPoint(null)));
       // 测试 referrer 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setReferrer(null)));
       // 测试 gender 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setGender(null)));
       // 测试 labelId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLabelId(null)));
       // 测试 createTime 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setCreateTime(null)));
       // 准备参数
       MemberUserPageReqVO reqVO = new MemberUserPageReqVO();
       reqVO.setNickname(null);
       reqVO.setRealName(null);
       reqVO.setAvatar(null);
       reqVO.setStatus(null);
       reqVO.setMobile(null);
       reqVO.setPassword(null);
       reqVO.setRegisterIp(null);
       reqVO.setLoginIp(null);
       reqVO.setPayPassword(null);
       reqVO.setLoginDate(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setAreaId(null);
       reqVO.setUserGroupId(null);
       reqVO.setPoint(null);
       reqVO.setReferrer(null);
       reqVO.setGender(null);
       reqVO.setLabelId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<MemberUserDO> pageResult = userService.getUserPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbUser, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUserList() {
       // mock 数据
       MemberUserDO dbUser = randomPojo(MemberUserDO.class, o -> { // 等会查询到
           o.setNickname(null);
           o.setRealName(null);
           o.setAvatar(null);
           o.setStatus(null);
           o.setMobile(null);
           o.setPassword(null);
           o.setRegisterIp(null);
           o.setLoginIp(null);
           o.setPayPassword(null);
           o.setLoginDate(null);
           o.setAreaId(null);
           o.setUserGroupId(null);
           o.setPoint(null);
           o.setReferrer(null);
           o.setGender(null);
           o.setLabelId(null);
           o.setCreateTime(null);
       });
       userMapper.insert(dbUser);
       // 测试 nickname 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setNickname(null)));
       // 测试 realName 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setRealName(null)));
       // 测试 avatar 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setAvatar(null)));
       // 测试 status 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setStatus(null)));
       // 测试 mobile 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setMobile(null)));
       // 测试 password 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPassword(null)));
       // 测试 registerIp 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setRegisterIp(null)));
       // 测试 loginIp 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLoginIp(null)));
       // 测试 payPassword 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPayPassword(null)));
       // 测试 loginDate 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLoginDate(null)));
       // 测试 areaId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setAreaId(null)));
       // 测试 userGroupId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setUserGroupId(null)));
       // 测试 point 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setPoint(null)));
       // 测试 referrer 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setReferrer(null)));
       // 测试 gender 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setGender(null)));
       // 测试 labelId 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setLabelId(null)));
       // 测试 createTime 不匹配
       userMapper.insert(cloneIgnoreId(dbUser, o -> o.setCreateTime(null)));
       // 准备参数
       MemberUserExportReqVO reqVO = new MemberUserExportReqVO();
       reqVO.setNickname(null);
       reqVO.setRealName(null);
       reqVO.setAvatar(null);
       reqVO.setStatus(null);
       reqVO.setMobile(null);
       reqVO.setPassword(null);
       reqVO.setRegisterIp(null);
       reqVO.setLoginIp(null);
       reqVO.setPayPassword(null);
       reqVO.setLoginDate(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setAreaId(null);
       reqVO.setUserGroupId(null);
       reqVO.setPoint(null);
       reqVO.setReferrer(null);
       reqVO.setGender(null);
       reqVO.setLabelId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<MemberUserDO> list = userService.getExportUserList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbUser, list.get(0));
    }

}
