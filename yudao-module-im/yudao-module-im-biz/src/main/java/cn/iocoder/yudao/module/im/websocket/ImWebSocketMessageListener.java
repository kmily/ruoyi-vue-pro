package cn.iocoder.yudao.module.im.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.session.WebSocketSessionManager;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxSaveReqVO;
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

import java.util.List;

/**
 * WebSocket im
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class ImWebSocketMessageListener implements WebSocketMessageListener<ImSendMessage> {

    @Resource
    private WebSocketMessageSender webSocketMessageSender;
    @Resource
    private ImMessageService imMessageService;
    @Resource
    private ImConversationService imConversationService;
    @Resource
    private ImInboxService imInboxService;
    @Resource
    private SequenceGeneratorRedisDao sequenceGeneratorRedisDao;
    @Resource
    private WebSocketSessionManager webSocketSessionManager;

    @Override
    public void onMessage(WebSocketSession session, ImSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session);
        //1、插入消息表
        Long messageId = imMessageService.savePrivateMessage(message, fromUserId);

        // 私聊
        if (message.getConversationType().equals(ImConversationTypeEnum.PRIVATE.getType())) {
            //2、插入收件箱表（私聊：两条，群聊：每个群有一条）
            imInboxService.createInbox(new ImInboxSaveReqVO(message.getReceiverId(), messageId, sequenceGeneratorRedisDao.generateSequence(message.getReceiverId())));
            imInboxService.createInbox(new ImInboxSaveReqVO(fromUserId, messageId, sequenceGeneratorRedisDao.generateSequence(fromUserId)));

            //3、推送消息
            // 3.1判断是否在线
            List<WebSocketSession> sessions = (List<WebSocketSession>) webSocketSessionManager.getSessionList(UserTypeEnum.ADMIN.getValue(), message.getReceiverId());
            if (sessions.isEmpty()) {
                //更新消息状态,为发送失败
                imMessageService.updateMessageStatus(messageId, ImMessageStatusEnum.FAILURE.getStatus());
                return;
            }
            //3.2发送
            ImReceiveMessage toMessage = new ImReceiveMessage();
            toMessage.setFromId(fromUserId);
            toMessage.setConversationType(ImConversationTypeEnum.PRIVATE.getType());
            toMessage.setContentType(message.getContentType());
            toMessage.setContent(message.getContent());
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), message.getReceiverId(), // 给指定用户
                    "im-message-receive", toMessage);
            //4、更新消息状态,为发送成功
            imMessageService.updateMessageStatus(messageId, ImMessageStatusEnum.SUCCESS.getStatus());

        }
    }

    @Override
    public String getType() {
        return "im-message-send";
    }

}
