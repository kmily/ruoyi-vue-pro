package cn.iocoder.yudao.module.im.service.groupmember;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.im.controller.admin.groupmember.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.groupmember.GroupMemberDO;
import cn.iocoder.yudao.module.im.dal.mysql.groupmember.ImGroupMemberMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ImGroupMemberServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ImGroupMemberServiceImpl.class)
public class ImGroupMemberServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ImGroupMemberServiceImpl groupMemberService;

    @Resource
    private ImGroupMemberMapper imGroupMemberMapper;

    @Test
    public void testCreateGroupMember_success() {
        // 准备参数
        ImGroupMemberSaveReqVO createReqVO = randomPojo(ImGroupMemberSaveReqVO.class).setId(null);

        // 调用
        Long groupMemberId = groupMemberService.createGroupMember(createReqVO);
        // 断言
        assertNotNull(groupMemberId);
        // 校验记录的属性是否正确
        GroupMemberDO groupMember = imGroupMemberMapper.selectById(groupMemberId);
        assertPojoEquals(createReqVO, groupMember, "id");
    }

    @Test
    public void testUpdateGroupMember_success() {
        // mock 数据
        GroupMemberDO dbGroupMember = randomPojo(GroupMemberDO.class);
        imGroupMemberMapper.insert(dbGroupMember);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ImGroupMemberSaveReqVO updateReqVO = randomPojo(ImGroupMemberSaveReqVO.class, o -> {
            o.setId(dbGroupMember.getId()); // 设置更新的 ID
        });

        // 调用
        groupMemberService.updateGroupMember(updateReqVO);
        // 校验是否更新正确
        GroupMemberDO groupMember = imGroupMemberMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, groupMember);
    }

    @Test
    public void testUpdateGroupMember_notExists() {
        // 准备参数
        ImGroupMemberSaveReqVO updateReqVO = randomPojo(ImGroupMemberSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> groupMemberService.updateGroupMember(updateReqVO), GROUP_MEMBER_NOT_EXISTS);
    }

    @Test
    public void testDeleteGroupMember_success() {
        // mock 数据
        GroupMemberDO dbGroupMember = randomPojo(GroupMemberDO.class);
        imGroupMemberMapper.insert(dbGroupMember);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbGroupMember.getId();

        // 调用
        groupMemberService.deleteGroupMember(id);
       // 校验数据不存在了
       assertNull(imGroupMemberMapper.selectById(id));
    }

    @Test
    public void testDeleteGroupMember_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> groupMemberService.deleteGroupMember(id), GROUP_MEMBER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetGroupMemberPage() {
       // mock 数据
       GroupMemberDO dbGroupMember = randomPojo(GroupMemberDO.class, o -> { // 等会查询到
           o.setGroupId(null);
           o.setUserId(null);
           o.setNickname(null);
           o.setAvatar(null);
           o.setAliasName(null);
           o.setRemark(null);
           o.setCreateTime(null);
       });
       imGroupMemberMapper.insert(dbGroupMember);
       // 测试 groupId 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setGroupId(null)));
       // 测试 userId 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setUserId(null)));
       // 测试 nickname 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setNickname(null)));
       // 测试 avatar 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setAvatar(null)));
       // 测试 aliasName 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setAliasName(null)));
       // 测试 remark 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       imGroupMemberMapper.insert(cloneIgnoreId(dbGroupMember, o -> o.setCreateTime(null)));
       // 准备参数
       ImGroupMemberPageReqVO reqVO = new ImGroupMemberPageReqVO();
       reqVO.setGroupId(null);
       reqVO.setUserId(null);
       reqVO.setNickname(null);
       reqVO.setAvatar(null);
       reqVO.setAliasName(null);
       reqVO.setRemark(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<GroupMemberDO> pageResult = groupMemberService.getGroupMemberPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbGroupMember, pageResult.getList().get(0));
    }

}