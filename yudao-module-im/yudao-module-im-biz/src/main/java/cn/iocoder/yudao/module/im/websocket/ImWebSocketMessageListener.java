package cn.iocoder.yudao.module.im.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.redis.inbox.SequenceGeneratorRedisDao;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import cn.iocoder.yudao.module.im.service.conversation.ImConversationService;
import cn.iocoder.yudao.module.im.service.inbox.ImInboxService;
import cn.iocoder.yudao.module.im.service.message.ImMessageService;
import cn.iocoder.yudao.module.im.websocket.message.ImReceiveMessage;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket im
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class ImWebSocketMessageListener implements WebSocketMessageListener<ImSendMessage> {

    @Resource
    private WebSocketMessageSender webSocketMessageSender; // WebSocket消息发送器
    @Resource
    private ImMessageService imMessageService; // IM消息服务
    @Resource
    private ImConversationService imConversationService; // IM会话服务
    @Resource
    private ImInboxService imInboxService; // IM收件箱服务
    @Resource
    private SequenceGeneratorRedisDao sequenceGeneratorRedisDao; // 序列生成器Redis DAO

    /**
     * 处理WebSocket消息
     *
     * @param session WebSocket会话
     * @param message 发送的IM消息
     */
    @Override
    public void onMessage(WebSocketSession session, ImSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session); // 获取登录用户ID

        // 如果是私人消息，处理私人消息
        if (message.getConversationType().equals(ImConversationTypeEnum.PRIVATE.getType())) {
            ImMessageDO imMessageDO = imMessageService.savePrivateMessage(message, fromUserId); // 保存私人消息
            handlePrivateMessage(fromUserId, imMessageDO, message);
        }
    }

    /**
     * 处理私人消息
     *
     * @param fromUserId  发送者用户ID
     * @param imMessageDO IM消息数据对象
     * @param message     发送的IM消息
     */
    private void handlePrivateMessage(Long fromUserId, ImMessageDO imMessageDO, ImSendMessage message) {
        Long fromUserSequence = sequenceGeneratorRedisDao.generateSequence(fromUserId); // 生成发送者序列
        Long fromUserInboxId = createAndSaveInbox(fromUserId, imMessageDO.getId(), fromUserSequence); // 创建并保存发送者收件箱
        Long receiverSequence = sequenceGeneratorRedisDao.generateSequence(fromUserId); // 生成接收者序列
        Long receiverInboxId = createAndSaveInbox(message.getReceiverId(), imMessageDO.getId(), receiverSequence); // 创建并保存接收者收件箱

        // 发送消息给接收者和发送者
        sendMessage(fromUserId, receiverInboxId, imMessageDO, message, "im-message-receive", fromUserSequence);
        sendMessage(fromUserId, fromUserInboxId, imMessageDO, message, "im-message-receive", receiverSequence);

        // 更新消息状态为成功
        imMessageService.updateMessageStatus(imMessageDO.getId(), ImMessageStatusEnum.SUCCESS.getStatus());
        // 保存私人会话
        imConversationService.savePrivateConversation(fromUserId, message.getReceiverId());
    }

    /**
     * 创建并保存收件箱
     *
     * @param userId    用户ID
     * @param messageId 消息ID
     * @param sequence  序列
     * @return 收件箱ID
     */
    private Long createAndSaveInbox(Long userId, Long messageId, Long sequence) {
        ImInboxSaveReqVO inboxSaveReqVO = new ImInboxSaveReqVO(userId, messageId, sequence); // 创建收件箱保存请求VO
        return imInboxService.createInbox(inboxSaveReqVO); // 创建收件箱
    }

    /**
     * 发送消息
     *
     * @param fromUserId  发送者用户ID
     * @param inboxId     收件箱ID
     * @param imMessageDO IM消息数据对象
     * @param message     发送的IM消息
     * @param messageType 消息类型
     * @param sequence    序列
     */
    private void sendMessage(Long fromUserId, Long inboxId, ImMessageDO imMessageDO, ImSendMessage message, String messageType, Long sequence) {
        ImReceiveMessage receiveMessage = new ImReceiveMessage(); // 创建接收消息
        receiveMessage.setFromId(fromUserId); // 设置发送者ID
        receiveMessage.setConversationType(ImConversationTypeEnum.PRIVATE.getType()); // 设置会话类型为私人
        receiveMessage.setContentType(message.getContentType()); // 设置内容类型
        receiveMessage.setContent(message.getContent()); // 设置内容
        receiveMessage.setMessageId(imMessageDO.getId()); // 设置消息ID
        receiveMessage.setInboxId(inboxId); // 设置收件箱ID
        receiveMessage.setSendTime(imMessageDO.getSendTime()); // 设置发送时间
        receiveMessage.setSequence(sequence); // 设置序列
        webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), fromUserId, messageType, receiveMessage); // 发送消息
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return "im-message-send";
    }

}