package cn.iocoder.yudao.module.therapy.service.impl;

import cn.iocoder.yudao.module.therapy.service.AIChatService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zhipu.oapi.demo.V4OkHttpClientTest.mapStreamToAccumulator;

/**
 * Author:lidongw_1
 * Date 2024/5/24
 * Description: ZHIPU AI 大模型
 **/
@Slf4j
@Service
public class ZhiPuAIChatServiceImpl implements AIChatService {

    //@Value("${zhipu.api.key:this-is-a-test-key}")
    String API_KEY="7820338e5c0e1d9228f8c2a5e2bf2e0d.EIWUgIkQCoStAnBU";

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
    public String chat(Long userId, String content) {

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "你好，我是智能助手，有什么可以帮助你的吗？");
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        //messages.add(sysMessage);
        messages.add(chatMessage);
        return chat(messages);
    }

    @Override
    public String chatForStream(Long userId, String content) {
        return "";
    }

    private String chat(List<ChatMessage> messages) {
        System.out.println("chat input:" + JSON.toJSONString(messages));
        String requestIdTemplate = "chat-%s";
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        try {

            if (invokeModelApiResp.isSuccess()) {

            }

            String s = mapper.writeValueAsString(chatCompletionRequest);

            System.out.println("model input:" +  mapper.writeValueAsString(chatCompletionRequest));
            System.out.println("model output:" + mapper.writeValueAsString(invokeModelApiResp));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //ModelApiResponse sseModelApiResp = client.invokeModelApi(chatCompletionRequest);
        //if (sseModelApiResp.isSuccess()) {
        //    AtomicBoolean isFirst = new AtomicBoolean(true);
        //    List<Choice> choices = new ArrayList<>();
        //    ChatMessageAccumulator chatMessageAccumulator = mapStreamToAccumulator(sseModelApiResp.getFlowable()).doOnNext(accumulator -> {
        //        {
        //            if (isFirst.getAndSet(false)) {
        //                log.info("Response: ");
        //            }
        //            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null) {
        //                String jsonString = mapper.writeValueAsString(accumulator.getDelta().getTool_calls());
        //                log.info("tool_calls: {}", jsonString);
        //            }
        //            if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null) {
        //                log.info(accumulator.getDelta().getContent());
        //            }
        //            choices.add(accumulator.getChoice());
        //        }
        //    }).doOnComplete(System.out::println).lastElement().blockingGet();
        //
        //
        //    ModelData data = new ModelData();
        //    data.setChoices(choices);
        //    data.setUsage(chatMessageAccumulator.getUsage());
        //    data.setId(chatMessageAccumulator.getId());
        //    data.setCreated(chatMessageAccumulator.getCreated());
        //    data.setRequestId(chatCompletionRequest.getRequestId());
        //    sseModelApiResp.setFlowable(null);// 打印前置空
        //    sseModelApiResp.setData(data);
        //}
        //// logger.info("model output: {}", mapper.writeValueAsString(sseModelApiResp));
        //System.out.println("model output:" + JSON.toJSONString(sseModelApiResp));
        //// System.out.println("model output:" + mapper.writeValueAsString(invokeModelApiResp));

        throw new NotImplementedException("Not implemented");
    }

}
