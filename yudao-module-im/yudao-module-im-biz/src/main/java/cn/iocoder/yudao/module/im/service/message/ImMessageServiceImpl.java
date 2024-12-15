package cn.iocoder.yudao.module.im.service.message;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageListByNoReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageListReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSendReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.ImMessageMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageSourceEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import cn.iocoder.yudao.module.im.service.groupmember.ImGroupMemberService;
import cn.iocoder.yudao.module.im.service.inbox.ImInboxService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.MESSAGE_RECEIVER_NOT_EXISTS;
import static cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum.generateConversationNo;

/**
 * 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ImMessageServiceImpl implements ImMessageService {

    @Resource
    private ImMessageMapper imMessageMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ImInboxService imInboxService;
    @Resource
    private ImGroupMemberService imGroupMemberService;

    @Override
    public List<ImMessageDO> getMessageList(ImMessageListReqVO listReqVO) {
        // TODO @dylan：userId 从 controller 传递，service 保持无状态
        // 1. 获得会话编号
        Long loginUserId = getLoginUserId();
        // TODO @dylan：修复下 generateConversationNo 方法，fromUserId、receiverId 需要小的放前面，大的放后面。这样，可以保证一定只生成唯一的。
        String no1 = generateConversationNo(loginUserId, listReqVO.getReceiverId(), listReqVO.getConversationType());
        String no2 = generateConversationNo(listReqVO.getReceiverId(),loginUserId, listReqVO.getConversationType());

        // 2. 查询历史消息
        ImMessageDO message = new ImMessageDO()
                .setSendTime(listReqVO.getSendTime())
                .setConversationNo(no1);
        List<ImMessageDO>  list = imMessageMapper.selectMessageList(message);

        if (!list.isEmpty()) {
            return list;
        }

        message.setConversationNo(no2);
        list = imMessageMapper.selectMessageList(message);

        return list;
    }

    @Override
    public List<ImMessageDO> getMessageListByConversationNo(ImMessageListByNoReqVO listReqVO) {

        // 1. 查询历史消息
        ImMessageDO message = new ImMessageDO()
                .setSendTime(listReqVO.getSendTime())
                .setConversationNo(listReqVO.getConversationNo());
        return imMessageMapper.selectMessageList(message);
    }


    @Override
    public List<ImMessageDO> pullMessageList(Long userId, Long sequence, Integer size) {
        List<Long> messageIds = imInboxService.selectMessageIdsByUserIdAndSequence(userId, sequence, size);
        if (CollUtil.isEmpty(messageIds)) {
            return Collections.emptyList();
        }
        return imMessageMapper.selectBatchIds(messageIds);
    }

    @Override
    public ImMessageDO sendMessage(Long fromUserId, ImMessageSendReqVO imMessageSendReqVO) {
        // 1. 保存消息
        ImMessageDO message = saveMessage(fromUserId, imMessageSendReqVO);
        // 2. 保存到收件箱，并发送消息
        imInboxService.saveInboxAndSendMessage(message);
        return message;
    }

    private ImMessageDO saveMessage(Long fromUserId, ImMessageSendReqVO message) {
        // TODO 芋艿：消息格式的校验
        // 1. 校验接收人是否存在
        validateReceiverIdExists(message);
        // 2. 查询发送人信息
        AdminUserRespDTO fromUser = adminUserApi.getUser(fromUserId);

        // 3. 生成conversationNo
        String conversationNo = "";
        if (fromUserId == message.getConversationUserId()) {
            conversationNo = generateConversationNo(fromUserId, message.getReceiverId(), message.getConversationType());
        } else {
            conversationNo = generateConversationNo(message.getReceiverId(), fromUserId, message.getConversationType());
        }

        // 4. 保存消息
        ImMessageDO imMessageDO = BeanUtil.copyProperties(message, ImMessageDO.class)
                .setSenderNickname(fromUser.getNickname()).setSenderAvatar(fromUser.getAvatar())
                .setSenderId(fromUserId)
                .setConversationNo(conversationNo)
                .setSendFrom(ImMessageSourceEnum.USER_SEND.getSource())
                .setMessageStatus(ImMessageStatusEnum.SENDING.getStatus())
                .setSendTime(LocalDateTime.now());
        imMessageMapper.insert(imMessageDO);
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
            List<ImGroupMemberDO> imGroupMemberDOS = imGroupMemberService.selectByGroupId(message.getReceiverId());
            if (imGroupMemberDOS.isEmpty()) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        }
    }

}