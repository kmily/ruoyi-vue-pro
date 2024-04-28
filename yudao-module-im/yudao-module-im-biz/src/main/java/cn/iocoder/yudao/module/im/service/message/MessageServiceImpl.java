package cn.iocoder.yudao.module.im.service.message;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageListReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSendReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.MessageMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageSourceEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import cn.iocoder.yudao.module.im.service.groupmember.GroupMemberService;
import cn.iocoder.yudao.module.im.service.inbox.InboxService;
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

// TODO @hao：前缀 IM
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
    private AdminUserApi adminUserApi;
    @Resource
    private InboxService inboxService;
    @Resource
    private GroupMemberService groupMemberService;

    @Override
    public List<ImMessageDO> getMessageList(Long loginUserId, ImMessageListReqVO listReqVO) {
        // 1. 获得会话编号
        String no = ImConversationTypeEnum.generateConversationNo(loginUserId, listReqVO.getReceiverId(), listReqVO.getConversationType());
        // 2. 查询历史消息
        ImMessageDO message = new ImMessageDO()
                .setSendTime(listReqVO.getSendTime())
                .setConversationNo(no);
        return messageMapper.selectMessageList(message);
    }

    @Override
    public List<ImMessageDO> pullMessageList(Long userId, Long sequence, Integer size) {
        List<Long> messageIds = inboxService.selectMessageIdsByUserIdAndSequence(userId, sequence, size);
        return messageMapper.selectBatchIds(messageIds);
    }

    @Override
    public ImMessageDO sendMessage(Long fromUserId, ImMessageSendReqVO imMessageSendReqVO) {
        // 1. 保存消息
        ImMessageDO message = saveMessage(fromUserId, imMessageSendReqVO);
        // 2. 保存到收件箱，并发送消息
        inboxService.saveInboxAndSendMessage(message);
        return message;
    }

    private ImMessageDO saveMessage(Long fromUserId, ImMessageSendReqVO message) {
        // 1. 校验接收人是否存在
        validateReceiverIdExists(message);
        // 2. 查询发送人信息
        AdminUserRespDTO fromUser = adminUserApi.getUser(fromUserId);
        // 3. 保存消息
        ImMessageDO imMessageDO = BeanUtil.copyProperties(message, ImMessageDO.class)
                .setSenderNickname(fromUser.getNickname()).setSenderAvatar(fromUser.getAvatar())
                .setSenderId(fromUserId)
                .setConversationNo(ImConversationTypeEnum.generateConversationNo(fromUserId, message.getReceiverId(), message.getConversationType()))
                .setSendFrom(ImMessageSourceEnum.USER_SEND.getSource())
                .setMessageStatus(ImMessageStatusEnum.SENDING.getStatus())
                .setSendTime(TimeUtil.now());
        messageMapper.insert(imMessageDO);
        return imMessageDO;
    }

    private void validateReceiverIdExists(ImMessageSendReqVO message) {
        if (message.getConversationType().equals(ImConversationTypeEnum.SINGLE.getType())) {
            // 校验用户是否存在
            AdminUserRespDTO receiverUser = adminUserApi.getUser(message.getReceiverId());
            if (receiverUser == null) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        } else if (message.getConversationType().equals(ImConversationTypeEnum.GROUP.getType())) {
            // 校验群聊是否存在
            List<ImGroupMemberDO> imGroupMemberDOS = groupMemberService.selectByGroupId(message.getReceiverId());
            if (imGroupMemberDOS.isEmpty()) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        }
    }

}