package cn.iocoder.yudao.module.therapy.service.impl;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.therapy.controller.admin.VO.AutomaticThinkingRecognitionChatHistoriesVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.chat.ChatMessageDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.chat.ChatMessageMapper;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
import cn.iocoder.yudao.module.therapy.service.enums.UserTypeEnum;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.collect.Lists;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageAccumulator;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.Choice;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import liquibase.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author:lidongw_1
 * Date 2024/5/24
 * Description: ZHIPU AI 大模型
 **/
@Slf4j
@Service
public class ZhiPuAIChatServiceImpl implements AIChatService {


    @Resource
    private DictDataService dictDataService;

    @Resource
    ChatMessageMapper chatMessageMapper;

    //@Value("${zhipu.api.key:this-is-a-test-key}")
    String API_KEY = "7820338e5c0e1d9228f8c2a5e2bf2e0d.EIWUgIkQCoStAnBU";

    private ClientV4 client;
    @Autowired
    private JsonUtils jsonUtils;

    @PostConstruct
    public void init() {
        client = new ClientV4.Builder(API_KEY).build();
    }

    private static final ObjectMapper mapper = defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    @Override
    public String chat(Long userId, String conversationId, String content) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "你好，我是智能助手，有什么可以帮助你的吗？");
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

        if (chat == null) {
            return "我不知道你在说什么";
        }

        if (!chat.isSuccess()) {
            log.error("chat error:{}", JSON.toJSONString(chat));
            return "刚才说的没有理解，你可以在说一遍吗？";
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            return "我不知道你在说什么";
        }
        return choices.get(0).getDelta().getContent();
    }

    @Override
    public String chatForStream(Long userId, String conversationId, String content) {
        return "";
    }

    @Override
    public String dataAnalyse(String sysPrompt, String analyseData) {

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), sysPrompt);
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), analyseData);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

        if (chat == null) {
            log.error("chat error:{}", "系统返回为NULL");
            return "我不知道你在说什么";
        }

        if (!chat.isSuccess()) {
            log.error("chat error:{}", JSON.toJSONString(chat));
            return "刚才说的没有理解，你可以在说一遍吗？";
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat error:{}", "choices为空");
            return "我不知道你在说什么";
        }
        return choices.get(0).getDelta().getContent();

    }

    /**
     * 青少年问题分类
     *
     * @param problems 问题
     * @return
     */
    @Override
    public String teenProblemClassification(String problems) {
        ModelApiResponse chat = problemClassification(problems);
        if (chat == null) return "系统提示未配置";

        if (!chat.isSuccess()) {
            log.error("chat problem:{} error:{}", problems, JSON.toJSONString(chat));
            return "解析失败：" + chat.getMsg();
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat problem:{} error:{}", problems, "choices为空");
            return "解析失败，choices为空";
        }
        return String.valueOf(choices.get(0).getMessage().getContent());
    }

    private ModelApiResponse problemClassification(String problems) {
        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "juvenile_problems_classification");
        if (dictDataDO == null) {
            log.error("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return null;
        }

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), dictDataDO.getValue());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), problems);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);
        return chat;
    }

    @Override
    public List<String> teenProblemClassificationV2(String problems) {
        ModelApiResponse modelApiResponse = problemClassification(problems);

        if (modelApiResponse == null) {
            log.error("chat problem:{} error:{}", problems, "系统返回为NULL");
            return Collections.emptyList();
        }

        if (!modelApiResponse.isSuccess()) {
            log.error("chat problem:{} error:{}", problems, JSON.toJSONString(modelApiResponse));
            return Collections.emptyList();
        }

        ModelData data = modelApiResponse.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat problem:{} error:{}", problems, "choices为空");
            return Collections.emptyList();
        }

        String s = String.valueOf(choices.get(0).getMessage().getContent());
        if (JsonUtils.isJson(s)) {
            List<String> list = JsonUtils.parseArray(s, String.class);
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 自动化思维识别
     *
     * @param userId         用户编号
     * @param conversationId 会话编号
     * @param content        用户聊天问题
     * @return
     */
    @Override
    public Flux<SSEMsgDTO> automaticThinkingRecognition(Long userId, String conversationId, String content) {

        //保存用户消息
        saveUserChatMessage(conversationId, userId,0L, UserTypeEnum.USER, content);
        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "automatic_thinking_recognition_prompt");
        if (dictDataDO == null) {
            SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
            sseMsgDTO.setContent("----END----");
            sseMsgDTO.setFinished(true);
            sseMsgDTO.setCode(500);
            sseMsgDTO.setMsg("系统提示未配置");
            log.warn("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return Flux.just(sseMsgDTO);
        }
        List<ChatMessage> chatMessages = assembleHistoryList(conversationId,dictDataDO.getValue());
;


        ModelApiResponse sseModelApiResp = chat(chatMessages, true);
        if (sseModelApiResp.isSuccess()) {
            StringBuffer sb =new StringBuffer();
            Flowable<SSEMsgDTO> flowable = sseModelApiResp.getFlowable().map(modelData -> {
                // Convert ModelData to JSON string or desired string format
                SSEMsgDTO jsonString = convertModelDataToString(modelData);
                // Save to database
                //saveToDatabase(jsonString);
                log.info("AI Response :{}", JSON.toJSONString(jsonString));
                sb.append(jsonString.getContent());
                return jsonString;
            });
            return Flux.create(emitter -> flowable.subscribe(emitter::next, emitter::error,()->{
                SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
                sseMsgDTO.setContent("----END----");
                sseMsgDTO.setFinished(true);
                sseMsgDTO.setCode(200);
                emitter.next(sseMsgDTO);
                saveUserChatMessage(conversationId, 0L,userId, UserTypeEnum.ROBOT, sb.toString());
                emitter.complete();
            })).publishOn(Schedulers.boundedElastic()).cast(SSEMsgDTO.class);
        } else {
            return Flux.error(new RuntimeException("API call failed")).cast(SSEMsgDTO.class);
        }
    }

    @Override
    public List<ChatMessageDO> queryChatHistories(Long userId, Integer pageNo, Integer pageSize) {

        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_user_id",userId);
        queryWrapper.or();
        queryWrapper.eq("receive_user_id",userId);
        queryWrapper.orderByDesc("id");
        //分页
        queryWrapper.last("limit " + (pageNo - 1) * pageSize + "," + pageSize);
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectList(queryWrapper);

        return chatMessageDOS;
    }

    @Override
    public Long queryChatHistoriesCount(Long userId) {
        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_user_id",userId);
        queryWrapper.or();
        queryWrapper.eq("receive_user_id",userId);
        Long l = chatMessageMapper.selectCount(queryWrapper);
        return l;
    }

    private List<ChatMessage> assembleHistoryList(String conversationId,String sysPrompt){
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),sysPrompt );
        messages.add(sysMessage);

        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id",conversationId);
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectList(queryWrapper);
        for (ChatMessageDO chatMessageDO : chatMessageDOS) {
            ChatMessage chatMessage = new ChatMessage();
            if (UserTypeEnum.USER.name().compareToIgnoreCase(chatMessageDO.getSendUserType())==0) {
                chatMessage.setRole(ChatMessageRole.USER.value());
            } else {
                chatMessage.setRole(ChatMessageRole.ASSISTANT.value());
            }
            chatMessage.setContent(chatMessageDO.getContent());
            messages.add(chatMessage);
        }
        return messages;

    }


    /**
     * 保存用户聊天消息
     *
     * @param conversationId 会话编号
     * @param sendUserId     发送用户编号
     * @param userType       用户类型
     * @param message        消息内容
     */
    private void saveUserChatMessage(String conversationId, long sendUserId,long receiveUserId, UserTypeEnum userType,String message){

        ChatMessageDO info = ChatMessageDO.builder().sendUserId(sendUserId).
                sendUserType(userType.name()).conversationId(conversationId).content(message)
                .receiveUserId(receiveUserId)
                .build();
        chatMessageMapper.insert(info);
    }

    private SSEMsgDTO convertModelDataToString(ModelData modelData) {
        // Implement conversion logic
        SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
        try {
            Choice choice = modelData.getChoices().get(0);
            sseMsgDTO.setContent(choice.getDelta().getContent());
            sseMsgDTO.setId(modelData.getId());
            sseMsgDTO.setFinished(BooleanUtil.parseBoolean(choice.getFinishReason()));
            sseMsgDTO.setCode(200);
            return  sseMsgDTO;
        } catch (Exception e) {
            sseMsgDTO.setCode(500);
            sseMsgDTO.setContent("数据解析失败");
            sseMsgDTO.setMsg(e.getMessage());
            sseMsgDTO.setFinished(true);
        }
        return sseMsgDTO; // Simplified for this example
    }

    public static Flowable<ChatMessageAccumulator> mapStreamToAccumulator(Flowable<ModelData> flowable) {
        return flowable.map(chunk -> {
            return new ChatMessageAccumulator(chunk.getChoices().get(0).getDelta(), null, chunk.getChoices().get(0), chunk.getUsage(), chunk.getCreated(), chunk.getId());
        });
    }


    private ModelApiResponse chat(List<ChatMessage> messages, Boolean isStream) {
        System.out.println("chat input: " + JSON.toJSONString(messages));
        String requestIdTemplate = "chat-%s";
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(isStream)
                .invokeMethod(Constants.invokeMethod)
                .temperature(0.67f)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse modelApiResponse = client.invokeModelApi(chatCompletionRequest);

        log.info("chat response:{}", JSON.toJSONString(modelApiResponse));
        return modelApiResponse;
    }
}
