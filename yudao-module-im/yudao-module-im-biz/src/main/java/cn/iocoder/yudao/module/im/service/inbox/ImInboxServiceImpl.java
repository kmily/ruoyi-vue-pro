package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSendMessageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.InboxDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.ImInboxMapper;
import cn.iocoder.yudao.module.im.dal.redis.inbox.InboxLockRedisDAO;
import cn.iocoder.yudao.module.im.dal.redis.inbox.SequenceRedisDao;
import cn.iocoder.yudao.module.im.enums.conversation.ConversationTypeEnum;
import cn.iocoder.yudao.module.im.service.groupmember.ImGroupMemberService;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.date.DateUnit;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.INBOX_NOT_EXISTS;

/**
 * 收件箱 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImInboxServiceImpl implements ImInboxService {

    private static final Long INBOX_LOCK_TIMEOUT = 120 * DateUnit.SECOND.getMillis();
    private static final String IM_MESSAGE_RECEIVE = "im-message-receive";

    @Resource
    private ImInboxMapper imInboxMapper;
    @Resource
    private SequenceRedisDao sequenceRedisDao; // 序列生成器Redis DAO
    @Resource
    private InboxLockRedisDAO inboxLockRedisDAO; // 收件箱的锁 Redis DAO
    @Resource
    private WebSocketMessageSender webSocketMessageSender; // WebSocket消息发送器
    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Override
    public Long createInbox(InboxSaveReqVO createReqVO) {
        InboxDO inbox = BeanUtils.toBean(createReqVO, InboxDO.class);
        imInboxMapper.insert(inbox);
        return inbox.getId();
    }

    @Override
    public void updateInbox(InboxSaveReqVO updateReqVO) {
        // 校验存在
        validateInboxExists(updateReqVO.getId());
        // 更新
        InboxDO updateObj = BeanUtils.toBean(updateReqVO, InboxDO.class);
        imInboxMapper.updateById(updateObj);
    }

    @Override
    public void deleteInbox(Long id) {
        // 校验存在
        validateInboxExists(id);
        // 删除
        imInboxMapper.deleteById(id);
    }

    private void validateInboxExists(Long id) {
        if (imInboxMapper.selectById(id) == null) {
            throw exception(INBOX_NOT_EXISTS);
        }
    }

    @Override
    public InboxDO getInbox(Long id) {
        return imInboxMapper.selectById(id);
    }

    @Override
    public PageResult<InboxDO> getInboxPage(ImInboxPageReqVO pageReqVO) {
        return imInboxMapper.selectPage(pageReqVO);
    }

    @Override
    public void saveInboxAndSendMessage(InboxSaveMessageReqVO inboxSaveMessage) {
        // 保存收件箱 + 发送消息给用户
        saveInboxAndSendMessageForUser(inboxSaveMessage.getFromId(), inboxSaveMessage);

        if (inboxSaveMessage.getConversationType().equals(ConversationTypeEnum.SINGLE.getType())) {
            saveInboxAndSendMessageForUser(inboxSaveMessage.getReceiverId(), inboxSaveMessage);
        } else if (inboxSaveMessage.getConversationType().equals(ConversationTypeEnum.GROUP.getType())) {
            List<GroupMemberDO> groupMembers = imGroupMemberService.selectByGroupId(inboxSaveMessage.getReceiverId());
            groupMembers.forEach(groupMemberDO -> saveInboxAndSendMessageForUser(groupMemberDO.getUserId(), inboxSaveMessage));
        }
    }

    private void saveInboxAndSendMessageForUser(Long userId, InboxSaveMessageReqVO inboxSaveMessage) {
        inboxLockRedisDAO.lock(userId, INBOX_LOCK_TIMEOUT, () -> {
            Long userSequence = sequenceRedisDao.generateSequence(userId);
            InboxDO inbox = new InboxDO();
            inbox.setUserId(userId);
            inbox.setMessageId(inboxSaveMessage.getMessageId());
            inbox.setSequence(userSequence);
            imInboxMapper.insert(inbox);

            //是发送人不发送
            if (userId.equals(inboxSaveMessage.getFromId())) {
                return;
            }
            InboxSendMessageReqVO message = BeanUtils.toBean(inboxSaveMessage, InboxSendMessageReqVO.class);
            message.setSequence(userSequence);
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), userId, IM_MESSAGE_RECEIVE, message);
        });
    }

}