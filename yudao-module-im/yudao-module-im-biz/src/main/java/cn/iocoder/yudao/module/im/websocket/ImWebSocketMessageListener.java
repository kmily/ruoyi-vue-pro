package cn.iocoder.yudao.module.im.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.service.conversation.ImConversationService;
import cn.iocoder.yudao.module.im.service.message.ImMessageService;
import cn.iocoder.yudao.module.im.websocket.message.ImReceiveMessage;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
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
    @Resource
    private ImMessageService imMessageService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ImConversationService imConversationService;

    @Override
    public void onMessage(WebSocketSession session, ImSendMessage message) {
        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session);
        //1、插入消息表
        ImMessageSaveReqVO imMessageSaveReqVO = new ImMessageSaveReqVO();
        imMessageSaveReqVO.setClientMessageId(message.getClientMessageId());
        imMessageSaveReqVO.setSenderId(fromUserId);
        imMessageSaveReqVO.setReceiverId(message.getReceiverId());
        //查询发送人昵称和发送人头像
        AdminUserRespDTO user = adminUserApi.getUser(fromUserId);
        imMessageSaveReqVO.setSenderNickname(user.getNickname());
        imMessageSaveReqVO.setSenderAvatar(user.getAvatar());
        imMessageSaveReqVO.setConversationType(message.getConversationType());
        imMessageSaveReqVO.setContentType(message.getContentType());
        imMessageSaveReqVO.setConversationNo("1");
        imMessageSaveReqVO.setContent(message.getContent());
        //消息来源 100-用户发送；200-系统发送（一般是通知）；不能为空
        imMessageSaveReqVO.setSendFrom(100);
        imMessageService.createMessage(imMessageSaveReqVO);


        // 私聊
        if (message.getConversationType().equals(ImConversationTypeEnum.PRIVATE.getType())) {
            //2、插入收件箱表（私聊：两条，群聊：每个群有一条）
            ImConversationSaveReqVO imConversationSaveReqVO = new ImConversationSaveReqVO();
            imConversationSaveReqVO.setUserId(fromUserId);
            imConversationSaveReqVO.setConversationType(message.getConversationType());
            //单聊时，用户编号；群聊时，群编号
            imConversationSaveReqVO.setTargetId(message.getReceiverId()+"");
            //会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId
            imConversationSaveReqVO.setNo("s_" + fromUserId + "_" + message.getReceiverId());
            imConversationSaveReqVO.setPinned(false);
            imConversationService.createConversation(imConversationSaveReqVO);

            ImConversationSaveReqVO imConversationSaveReqVO1 = new ImConversationSaveReqVO();
            imConversationSaveReqVO1.setUserId(message.getReceiverId());
            imConversationSaveReqVO1.setConversationType(message.getConversationType());
            //单聊时，用户编号；群聊时，群编号
            imConversationSaveReqVO1.setTargetId(fromUserId+"");
            //会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId
            imConversationSaveReqVO1.setNo("s_" + message.getReceiverId() + "_" + fromUserId);
            imConversationSaveReqVO1.setPinned(false);
            imConversationService.createConversation(imConversationSaveReqVO1);


            //3、推送消息
            ImReceiveMessage toMessage = new ImReceiveMessage();
            toMessage.setFromId(fromUserId);
            toMessage.setConversationType(ImConversationTypeEnum.PRIVATE.getType());
            toMessage.setContentType(message.getContentType());
            toMessage.setContent(message.getContent());
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), message.getReceiverId(), // 给指定用户
                    "im-message-receive", toMessage);
        }
    }

    @Override
    public String getType() {
        return "im-message-send";
    }

}
