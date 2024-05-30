package cn.iocoder.yudao.module.therapy.service.impl;

import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageAccumulator;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.Choice;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import liquibase.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    //@Value("${zhipu.api.key:this-is-a-test-key}")
    String API_KEY = "7820338e5c0e1d9228f8c2a5e2bf2e0d.EIWUgIkQCoStAnBU";

    private ClientV4 client;

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
        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "juvenile_problems_classification");
        if (dictDataDO == null) {
            log.error("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return "系统提示未配置";
        }

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), dictDataDO.getValue());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), problems);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

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

    /**
     * 自动化思维识别
     *
     * @param userId         用户编号
     * @param conversationId 会话编号
     * @param content        用户聊天问题
     * @return
     */
    @Override
    public Flux<Object> automaticThinkingRecognition(Long userId, String conversationId, String content) {

        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "automatic_thinking_recognition_prompt");
        if (dictDataDO == null) {
            log.warn("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return Flux.just("系统提示未配置", "----END----");
        }

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), dictDataDO.getValue());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse sseModelApiResp = chat(messages, true);
        if (sseModelApiResp.isSuccess()) {
            Flowable<SSEMsgDTO> flowable = sseModelApiResp.getFlowable().map(modelData -> {
                // Convert ModelData to JSON string or desired string format
                SSEMsgDTO jsonString = convertModelDataToString(modelData);
                // Save to database
                //saveToDatabase(jsonString);
                log.info("AI Response :{}", JSON.toJSONString(jsonString));
                return jsonString;
            });

            log.info("------END----");
            return Flux.create(emitter -> flowable.subscribe(emitter::next, emitter::error, emitter::complete)).publishOn(Schedulers.boundedElastic());
        } else {
            return Flux.error(new RuntimeException("API call failed"));
        }
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
