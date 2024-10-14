package cn.iocoder.yudao.module.im.service.inbox;

import cn.hutool.core.date.DateUnit;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.ImInboxMapper;
import cn.iocoder.yudao.module.im.dal.redis.inbox.InboxLockRedisDAO;
import cn.iocoder.yudao.module.im.dal.redis.inbox.SequenceRedisDAO;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.service.groupmember.ImGroupMemberService;
import cn.iocoder.yudao.module.infra.api.websocket.WebSocketSenderApi;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
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
public class ImInboxServiceImpl implements ImInboxService {

    private static final Long INBOX_LOCK_TIMEOUT = 120 * DateUnit.SECOND.getMillis();
    private static final String IM_MESSAGE_RECEIVE = "im-message-receive";

    @Resource
    private ImInboxMapper imInboxMapper;

    @Resource
    private SequenceRedisDAO sequenceRedisDAO;
    @Resource
    private InboxLockRedisDAO inboxLockRedisDAO;

    @Resource
    private WebSocketSenderApi webSocketSenderApi;
    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Override
    public void saveInboxAndSendMessage(ImMessageDO message) {
        // 1. 保存收件箱 + 发送消息给发送人
        saveInboxAndSendMessageForUser(message.getSenderId(), message);
        // 2. 保存收件箱 + 发送消息给接收人
        if (message.getConversationType().equals(ImConversationTypeEnum.SINGLE.getType())) {
            // 2.1 如果是单聊，直接发送给接收人
            saveInboxAndSendMessageForUser(message.getReceiverId(), message);
        } else if (message.getConversationType().equals(ImConversationTypeEnum.GROUP.getType())) {
            // 2.2 如果是群聊，发送给群聊的所有人
            List<ImGroupMemberDO> groupMembers = imGroupMemberService.selectByGroupId(message.getReceiverId());
            groupMembers.forEach(groupMemberDO -> saveInboxAndSendMessageForUser(groupMemberDO.getUserId(), message));
        }
    }

    @Override
    public List<Long> selectMessageIdsByUserIdAndSequence(Long userId, Long sequence, Integer size) {
        List<ImInboxDO> imInboxDOS = imInboxMapper.selectListByUserIdAndSequence(userId, sequence, size);
        return imInboxDOS.stream().map(ImInboxDO::getMessageId).toList();
    }


    //TODO 多线程处理
    public void saveInboxAndSendMessageForUser(Long userId, ImMessageDO message) {
        inboxLockRedisDAO.lock(userId, INBOX_LOCK_TIMEOUT, () -> {
            // 1. 生成序列号
            Long userSequence = sequenceRedisDAO.generateSequence(userId);
            // 2. 保存收件箱
            ImInboxDO inbox = new ImInboxDO()
                    .setUserId(userId)
                    .setMessageId(message.getId())
                    .setSequence(userSequence);
            imInboxMapper.insert(inbox);
            // 3. 发送消息
            sendAsyncMessage(userId, message, userSequence);
        });
    }

    @Async
    public void sendAsyncMessage(Long userId, ImMessageDO message, Long userSequence) {
        ImMessageRespVO messageRespVO = BeanUtils.toBean(message, ImMessageRespVO.class);
        messageRespVO.setSequence(userSequence);
        webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId, IM_MESSAGE_RECEIVE, messageRespVO);
    }

}