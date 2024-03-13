package cn.iocoder.yudao.module.im.service.inbox;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.InboxMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ImInboxServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ImInboxServiceImpl.class)
public class ImInboxServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ImInboxServiceImpl inboxService;

    @Resource
    private InboxMapper inboxMapper;

    @Test
    public void testCreateInbox_success() {
        // 准备参数
        ImInboxSaveReqVO createReqVO = randomPojo(ImInboxSaveReqVO.class).setId(null);

        // 调用
        Long inboxId = inboxService.createInbox(createReqVO);
        // 断言
        assertNotNull(inboxId);
        // 校验记录的属性是否正确
        ImInboxDO inbox = inboxMapper.selectById(inboxId);
        assertPojoEquals(createReqVO, inbox, "id");
    }

    @Test
    public void testUpdateInbox_success() {
        // mock 数据
        ImInboxDO dbInbox = randomPojo(ImInboxDO.class);
        inboxMapper.insert(dbInbox);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ImInboxSaveReqVO updateReqVO = randomPojo(ImInboxSaveReqVO.class, o -> {
            o.setId(dbInbox.getId()); // 设置更新的 ID
        });

        // 调用
        inboxService.updateInbox(updateReqVO);
        // 校验是否更新正确
        ImInboxDO inbox = inboxMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, inbox);
    }

    @Test
    public void testUpdateInbox_notExists() {
        // 准备参数
        ImInboxSaveReqVO updateReqVO = randomPojo(ImInboxSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> inboxService.updateInbox(updateReqVO), INBOX_NOT_EXISTS);
    }

    @Test
    public void testDeleteInbox_success() {
        // mock 数据
        ImInboxDO dbInbox = randomPojo(ImInboxDO.class);
        inboxMapper.insert(dbInbox);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbInbox.getId();

        // 调用
        inboxService.deleteInbox(id);
       // 校验数据不存在了
       assertNull(inboxMapper.selectById(id));
    }

    @Test
    public void testDeleteInbox_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> inboxService.deleteInbox(id), INBOX_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetInboxPage() {
       // mock 数据
       ImInboxDO dbInbox = randomPojo(ImInboxDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setMessageId(null);
           o.setSequence(null);
           o.setCreateTime(null);
       });
       inboxMapper.insert(dbInbox);
       // 测试 userId 不匹配
       inboxMapper.insert(cloneIgnoreId(dbInbox, o -> o.setUserId(null)));
       // 测试 messageId 不匹配
       inboxMapper.insert(cloneIgnoreId(dbInbox, o -> o.setMessageId(null)));
       // 测试 sequence 不匹配
       inboxMapper.insert(cloneIgnoreId(dbInbox, o -> o.setSequence(null)));
       // 测试 createTime 不匹配
       inboxMapper.insert(cloneIgnoreId(dbInbox, o -> o.setCreateTime(null)));
       // 准备参数
       ImInboxPageReqVO reqVO = new ImInboxPageReqVO();
       reqVO.setUserId(null);
       reqVO.setMessageId(null);
       reqVO.setSequence(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ImInboxDO> pageResult = inboxService.getInboxPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbInbox, pageResult.getList().get(0));
    }

}