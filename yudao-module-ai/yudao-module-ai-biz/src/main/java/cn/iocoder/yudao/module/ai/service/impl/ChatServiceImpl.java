package cn.iocoder.yudao.module.ai.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.iocoder.yudao.framework.ai.chat.ChatResponse;
import cn.iocoder.yudao.framework.ai.chat.messages.MessageType;
import cn.iocoder.yudao.framework.ai.chat.prompt.Prompt;
import cn.iocoder.yudao.framework.ai.config.AiClient;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ai.ErrorCodeConstants;
import cn.iocoder.yudao.module.ai.controller.Utf8SseEmitter;
import cn.iocoder.yudao.module.ai.dataobject.AiChatConversationDO;
import cn.iocoder.yudao.module.ai.dataobject.AiChatMessageDO;
import cn.iocoder.yudao.module.ai.dataobject.AiChatRoleDO;
import cn.iocoder.yudao.module.ai.enums.AiClientNameEnum;
import cn.iocoder.yudao.module.ai.enums.ChatConversationTypeEnum;
import cn.iocoder.yudao.module.ai.enums.ChatTypeEnum;
import cn.iocoder.yudao.module.ai.mapper.AiChatConversationMapper;
import cn.iocoder.yudao.module.ai.mapper.AiChatMessageMapper;
import cn.iocoder.yudao.module.ai.mapper.AiChatRoleMapper;
import cn.iocoder.yudao.module.ai.service.ChatService;
import cn.iocoder.yudao.module.ai.vo.ChatReq;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 聊天 service
 *
 * @author fansili
 * @time 2024/4/14 15:55
 * @since 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final AiClient aiClient;
    private final AiChatRoleMapper aiChatRoleMapper;
    private final AiChatMessageMapper aiChatMessageMapper;
    private final AiChatConversationMapper aiChatConversationMapper;


    /**
     * chat
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String chat(ChatReq req) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 获取 client 类型
        AiClientNameEnum clientNameEnum = AiClientNameEnum.valueOfName(req.getModal());
        // 获取 对话类型(新建还是继续)
        ChatConversationTypeEnum chatConversationTypeEnum = ChatConversationTypeEnum.valueOfType(req.getConversationType());
        AiChatConversationDO aiChatConversationDO = getChatConversationNoExistToCreate(req, chatConversationTypeEnum, loginUserId);

        // 保存 chat message
        saveChatMessage(req, aiChatConversationDO.getId(), loginUserId);

        String content = null;
        try {
            // 创建 chat 需要的 Prompt
            Prompt prompt = new Prompt(req.getPrompt());
            req.setTopK(req.getTopK());
            req.setTopP(req.getTopP());
            req.setTemperature(req.getTemperature());
            // 发送 call 调用
            ChatResponse call = aiClient.call(prompt, clientNameEnum.getName());
            content = call.getResult().getOutput().getContent();
        } catch (Exception e) {
            content = ExceptionUtil.getMessage(e);
        } finally {
            // 保存 chat message
            saveSystemChatMessage(req, aiChatConversationDO.getId(), loginUserId, content);
        }
        return content;
    }

    private void saveChatMessage(ChatReq req, Long chatConversationId, Long loginUserId) {
        // 增加 chat message 记录
        aiChatMessageMapper.insert(
                new AiChatMessageDO()
                        .setId(null)
                        .setChatConversationId(chatConversationId)
                        .setUserId(loginUserId)
                        .setMessage(req.getPrompt())
                        .setMessageType(MessageType.USER.getValue())
                        .setTopK(req.getTopK())
                        .setTopP(req.getTopP())
                        .setTemperature(req.getTemperature())
        );

        // chat count 先+1
        aiChatConversationMapper.updateIncrChatCount(req.getConversationId());
    }

    public void saveSystemChatMessage(ChatReq req, Long chatConversationId, Long loginUserId, String systemPrompts) {
        // 增加 chat message 记录
        aiChatMessageMapper.insert(
                new AiChatMessageDO()
                        .setId(null)
                        .setChatConversationId(chatConversationId)
                        .setUserId(loginUserId)
                        .setMessage(systemPrompts)
                        .setMessageType(MessageType.SYSTEM.getValue())
                        .setTopK(req.getTopK())
                        .setTopP(req.getTopP())
                        .setTemperature(req.getTemperature())
        );

        // chat count 先+1
        aiChatConversationMapper.updateIncrChatCount(req.getConversationId());
    }

    private AiChatConversationDO getChatConversationNoExistToCreate(ChatReq req, ChatConversationTypeEnum chatConversationTypeEnum, Long loginUserId) {
        AiChatConversationDO aiChatConversationDO;
        if (ChatConversationTypeEnum.NEW == chatConversationTypeEnum) {
            // 创建一个新的对话
            aiChatConversationDO = createNewChatConversation(req, loginUserId);
        } else {
            // 继续对话
            if (req.getConversationId() == null) {
                throw new ServerException(ErrorCodeConstants.AI_CHAT_CONTINUE_CONVERSATION_ID_NOT_NULL);
            }
            aiChatConversationDO = aiChatConversationMapper.selectById(req.getConversationId());
        }
        return aiChatConversationDO;
    }

    private AiChatConversationDO createNewChatConversation(ChatReq req, Long loginUserId) {
        // 获取 chat 角色
        String chatRoleName = null;
        ChatTypeEnum chatTypeEnum = null;
        Long chatRoleId = req.getChatRoleId();
        if (req.getChatRoleId() != null) {
            AiChatRoleDO aiChatRoleDO = aiChatRoleMapper.selectById(chatRoleId);
            if (aiChatRoleDO == null) {
                throw new ServerException(ErrorCodeConstants.AI_CHAT_ROLE_NOT_EXISTENT);
            }
            chatTypeEnum = ChatTypeEnum.ROLE_CHAT;
            chatRoleName = aiChatRoleDO.getRoleName();
        } else {
            chatTypeEnum = ChatTypeEnum.USER_CHAT;
        }
        //
        AiChatConversationDO insertChatConversation = new AiChatConversationDO()
                .setId(null)
                .setUserId(loginUserId)
                .setChatRoleId(req.getChatRoleId())
                .setChatRoleName(chatRoleName)
                .setChatType(chatTypeEnum.getType())
                .setChatCount(1)
                .setChatTitle(req.getPrompt().substring(0, 20) + "...");
        aiChatConversationMapper.insert(insertChatConversation);
        return insertChatConversation;
    }

    /**
     * chat stream
     *
     * @param req
     * @param sseEmitter
     * @return
     */
    @Override
    public void chatStream(ChatReq req, Utf8SseEmitter sseEmitter) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 获取 client 类型
        AiClientNameEnum clientNameEnum = AiClientNameEnum.valueOfName(req.getModal());
        // 获取 对话类型(新建还是继续)
        ChatConversationTypeEnum chatConversationTypeEnum = ChatConversationTypeEnum.valueOfType(req.getConversationType());
        AiChatConversationDO aiChatConversationDO = getChatConversationNoExistToCreate(req, chatConversationTypeEnum, loginUserId);
        // 创建 chat 需要的 Prompt
        Prompt prompt = new Prompt(req.getPrompt());
        req.setTopK(req.getTopK());
        req.setTopP(req.getTopP());
        req.setTemperature(req.getTemperature());
        // 保存 chat message
        saveChatMessage(req, aiChatConversationDO.getId(), loginUserId);
        Flux<ChatResponse> streamResponse = aiClient.stream(prompt, clientNameEnum.getName());

        StringBuffer contentBuffer = new StringBuffer();
        streamResponse.subscribe(
                new Consumer<ChatResponse>() {
                    @Override
                    public void accept(ChatResponse chatResponse) {
                        String content = chatResponse.getResults().get(0).getOutput().getContent();
                        try {
                            contentBuffer.append(content);
                            sseEmitter.send(content, MediaType.APPLICATION_JSON);
                        } catch (IOException e) {
                            log.error("发送异常{}", ExceptionUtil.getMessage(e));
                            // 如果不是因为关闭而抛出异常，则重新连接
                            sseEmitter.completeWithError(e);
                        }
                    }
                },
                error -> {
                    //
                    log.error("subscribe错误 {}", ExceptionUtil.getMessage(error));
                },
                () -> {
                    log.info("发送完成!");
                    sseEmitter.complete();
                    // 保存 chat message
                    saveSystemChatMessage(req, aiChatConversationDO.getId(), loginUserId, contentBuffer.toString());
                }
        );
    }
}