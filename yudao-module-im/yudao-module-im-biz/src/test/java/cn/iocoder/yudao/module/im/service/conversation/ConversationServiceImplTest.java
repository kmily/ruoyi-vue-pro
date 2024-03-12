package cn.iocoder.yudao.module.im.service.conversation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ConversationMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link ConversationServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ConversationServiceImpl.class)
public class ConversationServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ConversationServiceImpl conversationService;

    @Resource
    private ConversationMapper conversationMapper;

    @Test
    public void testCreateConversation_success() {
        // 准备参数
        ConversationSaveReqVO createReqVO = randomPojo(ConversationSaveReqVO.class).setId(null);

        // 调用
        Long conversationId = conversationService.createConversation(createReqVO);
        // 断言
        assertNotNull(conversationId);
        // 校验记录的属性是否正确
        ConversationDO conversation = conversationMapper.selectById(conversationId);
        assertPojoEquals(createReqVO, conversation, "id");
    }

    @Test
    public void testUpdateConversation_success() {
        // mock 数据
        ConversationDO dbConversation = randomPojo(ConversationDO.class);
        conversationMapper.insert(dbConversation);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ConversationSaveReqVO updateReqVO = randomPojo(ConversationSaveReqVO.class, o -> {
            o.setId(dbConversation.getId()); // 设置更新的 ID
        });

        // 调用
        conversationService.updateConversation(updateReqVO);
        // 校验是否更新正确
        ConversationDO conversation = conversationMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, conversation);
    }

    @Test
    public void testUpdateConversation_notExists() {
        // 准备参数
        ConversationSaveReqVO updateReqVO = randomPojo(ConversationSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> conversationService.updateConversation(updateReqVO), CONVERSATION_NOT_EXISTS);
    }

    @Test
    public void testDeleteConversation_success() {
        // mock 数据
        ConversationDO dbConversation = randomPojo(ConversationDO.class);
        conversationMapper.insert(dbConversation);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbConversation.getId();

        // 调用
        conversationService.deleteConversation(id);
       // 校验数据不存在了
       assertNull(conversationMapper.selectById(id));
    }

    @Test
    public void testDeleteConversation_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> conversationService.deleteConversation(id), CONVERSATION_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetConversationPage() {
       // mock 数据
       ConversationDO dbConversation = randomPojo(ConversationDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setConversationType(null);
           o.setTargetId(null);
           o.setNo(null);
           o.setPinned(null);
           o.setLastReadTime(null);
           o.setCreateTime(null);
       });
       conversationMapper.insert(dbConversation);
       // 测试 userId 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setUserId(null)));
       // 测试 conversationType 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setConversationType(null)));
       // 测试 targetId 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setTargetId(null)));
       // 测试 no 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setNo(null)));
       // 测试 pinned 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setPinned(null)));
       // 测试 lastReadTime 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setLastReadTime(null)));
       // 测试 createTime 不匹配
       conversationMapper.insert(cloneIgnoreId(dbConversation, o -> o.setCreateTime(null)));
       // 准备参数
       ConversationPageReqVO reqVO = new ConversationPageReqVO();
       reqVO.setUserId(null);
       reqVO.setConversationType(null);
       reqVO.setTargetId(null);
       reqVO.setNo(null);
       reqVO.setPinned(null);
       reqVO.setLastReadTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ConversationDO> pageResult = conversationService.getConversationPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbConversation, pageResult.getList().get(0));
    }

}