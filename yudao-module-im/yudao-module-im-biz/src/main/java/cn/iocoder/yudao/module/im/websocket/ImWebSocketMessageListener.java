package cn.iocoder.yudao.module.im.websocket;

import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import cn.iocoder.yudao.module.infra.api.websocket.WebSocketSenderApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

// TODO @hao：消息发送，使用 http 上行。因为在 cloud 框架下，我们比较难去 Listener。因为 im-server 不会自己启动 websocket 路径

/**
 * WebSocket im
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class ImWebSocketMessageListener implements WebSocketMessageListener<ImSendMessage> {

    public static final String IM_MESSAGE_RECEIVE = "im-message-receive";
    @Resource
    private WebSocketSenderApi webSocketSenderApi; // WebSocket消息发送器

    /**
     * 处理WebSocket消息
     *
     * @param session WebSocket会话
     * @param message 发送的IM消息
     */
    @Override
    public void onMessage(WebSocketSession session, ImSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session); // 获取登录用户ID
        log.info(message.toString());
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