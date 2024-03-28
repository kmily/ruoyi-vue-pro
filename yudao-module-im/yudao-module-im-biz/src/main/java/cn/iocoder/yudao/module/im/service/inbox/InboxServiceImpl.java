package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSendMessageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.InboxDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.InboxMapper;
import cn.iocoder.yudao.module.im.dal.redis.inbox.InboxLockRedisDAO;
import cn.iocoder.yudao.module.im.dal.redis.inbox.SequenceRedisDao;
import cn.iocoder.yudao.module.im.enums.conversation.ConversationTypeEnum;
import cn.iocoder.yudao.module.im.service.groupmember.GroupMemberService;
import cn.iocoder.yudao.module.infra.api.websocket.WebSocketSenderApi;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.date.DateUnit;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 收件箱 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InboxServiceImpl implements InboxService {

    private static final Long INBOX_LOCK_TIMEOUT = 120 * DateUnit.SECOND.getMillis();
    private static final String IM_MESSAGE_RECEIVE = "im-message-receive";

    @Resource
    private InboxMapper inboxMapper;
    @Resource
    private SequenceRedisDao sequenceRedisDao; // 序列生成器Redis DAO
    @Resource
    private InboxLockRedisDAO inboxLockRedisDAO; // 收件箱的锁 Redis DAO
    @Resource
    private WebSocketSenderApi webSocketSenderApi;
    @Resource
    private GroupMemberService groupMemberService;

    @Override
    public void saveInboxAndSendMessage(InboxSaveMessageReqVO inboxSaveMessage) {
        // 保存收件箱 + 发送消息给用户
        saveInboxAndSendMessageForUser(inboxSaveMessage.getFromId(), inboxSaveMessage);

        if (inboxSaveMessage.getConversationType().equals(ConversationTypeEnum.SINGLE.getType())) {
            saveInboxAndSendMessageForUser(inboxSaveMessage.getReceiverId(), inboxSaveMessage);
        } else if (inboxSaveMessage.getConversationType().equals(ConversationTypeEnum.GROUP.getType())) {
            List<GroupMemberDO> groupMembers = groupMemberService.selectByGroupId(inboxSaveMessage.getReceiverId());
            groupMembers.forEach(groupMemberDO -> saveInboxAndSendMessageForUser(groupMemberDO.getUserId(), inboxSaveMessage));
        }
    }

    @Override
    public List<Long> selectMessageIdsByUserIdAndSequence(Long userId, Long sequence, Integer size) {
        return inboxMapper.selectMessageIdsByUserIdAndSequence(userId, sequence, size);
    }

    private void saveInboxAndSendMessageForUser(Long userId, InboxSaveMessageReqVO inboxSaveMessage) {
        inboxLockRedisDAO.lock(userId, INBOX_LOCK_TIMEOUT, () -> {
            Long userSequence = sequenceRedisDao.generateSequence(userId);
            InboxDO inbox = new InboxDO();
            inbox.setUserId(userId);
            inbox.setMessageId(inboxSaveMessage.getMessageId());
            inbox.setSequence(userSequence);
            inboxMapper.insert(inbox);

            InboxSendMessageReqVO message = BeanUtils.toBean(inboxSaveMessage, InboxSendMessageReqVO.class);
            message.setSequence(userSequence);
            webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId, IM_MESSAGE_RECEIVE, message);
        });
    }

}