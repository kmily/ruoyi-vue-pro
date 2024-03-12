package cn.iocoder.yudao.module.im.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.websocket.message.ImReceiveMessage;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 示例：单发消息
 *
 * @author 芋道源码
 */
@Component
public class ImWebSocketMessageListener implements WebSocketMessageListener<ImSendMessage> {

    @Resource
    private WebSocketMessageSender webSocketMessageSender;

    @Override
    public void onMessage(WebSocketSession session, ImSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session);
        // 私聊
        if (message.getConversationType().equals(ImConversationTypeEnum.PRIVATE.getType())) {
            ImReceiveMessage toMessage = new ImReceiveMessage();
            toMessage.setToId(fromUserId);
            toMessage.setConversationType(ImConversationTypeEnum.PRIVATE.getType());
            //消息类型
            toMessage.setType(message.getType());
            toMessage.setBody(message.getBody());
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), message.getToId(), // 给指定用户
                    "im-message-receive", toMessage);
        }
    }

    @Override
    public String getType() {
        return "im-message-send";
    }

}
