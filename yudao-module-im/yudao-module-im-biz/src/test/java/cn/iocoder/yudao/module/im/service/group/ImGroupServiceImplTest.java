package cn.iocoder.yudao.module.im.service.group;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupDO;
import cn.iocoder.yudao.module.im.dal.mysql.group.ImGroupMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ImGroupServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ImGroupServiceImpl.class)
public class ImGroupServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ImGroupServiceImpl groupService;

    @Resource
    private ImGroupMapper imGroupMapper;

    @Test
    public void testCreateGroup_success() {
        // 准备参数
        ImGroupSaveReqVO createReqVO = randomPojo(ImGroupSaveReqVO.class).setId(null);

        // 调用
        Long groupId = groupService.createGroup(createReqVO);
        // 断言
        assertNotNull(groupId);
        // 校验记录的属性是否正确
        ImGroupDO group = imGroupMapper.selectById(groupId);
        assertPojoEquals(createReqVO, group, "id");
    }

    @Test
    public void testUpdateGroup_success() {
        // mock 数据
        ImGroupDO dbGroup = randomPojo(ImGroupDO.class);
        imGroupMapper.insert(dbGroup);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ImGroupSaveReqVO updateReqVO = randomPojo(ImGroupSaveReqVO.class, o -> {
            o.setId(dbGroup.getId()); // 设置更新的 ID
        });

        // 调用
        groupService.updateGroup(updateReqVO);
        // 校验是否更新正确
        ImGroupDO group = imGroupMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, group);
    }

    @Test
    public void testUpdateGroup_notExists() {
        // 准备参数
        ImGroupSaveReqVO updateReqVO = randomPojo(ImGroupSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> groupService.updateGroup(updateReqVO), GROUP_NOT_EXISTS);
    }

    @Test
    public void testDeleteGroup_success() {
        // mock 数据
        ImGroupDO dbGroup = randomPojo(ImGroupDO.class);
        imGroupMapper.insert(dbGroup);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbGroup.getId();

        // 调用
        groupService.deleteGroup(id);
       // 校验数据不存在了
       assertNull(imGroupMapper.selectById(id));
    }

    @Test
    public void testDeleteGroup_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> groupService.deleteGroup(id), GROUP_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetGroupPage() {
       // mock 数据
       ImGroupDO dbGroup = randomPojo(ImGroupDO.class, o -> { // 等会查询到
           o.setGroupName(null);
           o.setOwnerId(null);
           o.setHeadImage(null);
           o.setHeadImageThumb(null);
           o.setNotice(null);
           o.setRemark(null);
           o.setCreateTime(null);
       });
       imGroupMapper.insert(dbGroup);
       // 测试 groupName 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setGroupName(null)));
       // 测试 ownerId 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setOwnerId(null)));
       // 测试 headImage 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setHeadImage(null)));
       // 测试 headImageThumb 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setHeadImageThumb(null)));
       // 测试 notice 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setNotice(null)));
       // 测试 remark 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       imGroupMapper.insert(cloneIgnoreId(dbGroup, o -> o.setCreateTime(null)));
       // 准备参数
       ImGroupPageReqVO reqVO = new ImGroupPageReqVO();
       reqVO.setGroupName(null);
       reqVO.setOwnerId(null);
       reqVO.setHeadImage(null);
       reqVO.setHeadImageThumb(null);
       reqVO.setNotice(null);
       reqVO.setRemark(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ImGroupDO> pageResult = groupService.getGroupPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbGroup, pageResult.getList().get(0));
    }

}