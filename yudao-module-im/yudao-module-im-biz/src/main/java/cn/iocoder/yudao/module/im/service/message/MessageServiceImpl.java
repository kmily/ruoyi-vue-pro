package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.MessageMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageSourceEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageStatusEnum;
import cn.iocoder.yudao.module.im.service.groupmember.ImGroupMemberService;
import cn.iocoder.yudao.module.im.service.inbox.ImInboxService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.MESSAGE_RECEIVER_NOT_EXISTS;

/**
 * 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ImInboxService imInboxService; // IM收件箱服务
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Override
    public List<MessageDO> getMessagePage(MessagePageReqVO pageReqVO) {
        return messageMapper.getMessagePage(pageReqVO);
    }

    @Override
    public List<MessageDO> getMessageListBySequence(Long userId, Long sequence, Integer size) {
        return messageMapper.getGreaterThanSequenceMessage(userId, sequence, size);
    }

    @Override
    public SendMessageRespVO sendMessage(Long fromUserId, SendMessageReqVO message) {
        // 保存消息
        InboxSaveMessageReqVO inboxSaveMessageReqVO = new InboxSaveMessageReqVO();
        SendMessageRespVO sendMessageRespVO = saveMessage(fromUserId, message, inboxSaveMessageReqVO);

        // 保存收件箱 + 发送消息给用户
        imInboxService.saveInboxAndSendMessage(inboxSaveMessageReqVO);
        return sendMessageRespVO;
    }

    public SendMessageRespVO saveMessage(Long fromUserId, SendMessageReqVO message, InboxSaveMessageReqVO inboxSaveMessageReqVO) {
        //需要校验 receiverId 存在
        validateReceiverIdExists(message);
        // 查询发送人昵称和发送人头像
        AdminUserRespDTO fromUser = adminUserApi.getUser(fromUserId);
        // 使用链式调用创建 MessageDO 对象
        MessageDO messageDO = new MessageDO()
                .setClientMessageId(message.getClientMessageId())
                .setSenderId(fromUserId)
                .setReceiverId(message.getReceiverId())
                .setSenderNickname(fromUser.getNickname())
                .setSenderAvatar(fromUser.getAvatar())
                .setConversationType(message.getConversationType())
                .setContentType(message.getContentType())
                .setConversationNo(ConversationTypeEnum.generateConversationNo(fromUserId, message.getReceiverId(), message.getConversationType()))
                .setContent(message.getContent())
                .setSendFrom(MessageSourceEnum.USER_SEND.getStatus())
                .setSendTime(TimeUtil.now())
                .setMessageStatus(MessageStatusEnum.SENDING.getStatus());
        messageMapper.insert(messageDO);

        // 设置 InboxSaveMessageReqVO 对象
        inboxSaveMessageReqVO.setConversationType(message.getConversationType());
        inboxSaveMessageReqVO.setFromId(fromUserId);
        inboxSaveMessageReqVO.setReceiverId(message.getReceiverId());
        inboxSaveMessageReqVO.setMessageId(messageDO.getId());
        inboxSaveMessageReqVO.setContentType(message.getContentType());
        inboxSaveMessageReqVO.setContent(message.getContent());
        inboxSaveMessageReqVO.setSendTime(messageDO.getSendTime());

        // 返回 SendMessageRespVO 对象
        return new SendMessageRespVO(messageDO.getId(), messageDO.getSendTime());
    }

    private void validateReceiverIdExists(SendMessageReqVO message) {
        if (message.getReceiverId() == null) {
            throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
        }
        if (message.getConversationType().equals(ConversationTypeEnum.SINGLE.getType())) {
            //校验用户是否存在；
            AdminUserRespDTO receiverUser = adminUserApi.getUser(message.getReceiverId());
            if (receiverUser == null) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }

        } else if (message.getConversationType().equals(ConversationTypeEnum.GROUP.getType())) {
            //校验群聊是否存在；
            List<GroupMemberDO> groupMemberDOS = imGroupMemberService.selectByGroupId(message.getReceiverId());
            if (groupMemberDOS.isEmpty()) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        }
    }

}