package cn.iocoder.yudao.module.therapy.service.impl;

import cn.iocoder.yudao.module.therapy.service.AIChatService;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:lidongw_1
 * Date 2024/5/24
 * Description: ZHIPU AI 大模型
 **/

@Service
public class ZhiPuAIChatService implements AIChatService {





        @Override
        public String chat(Long userId, String content) {

            return null;
        }


        private String chat(List<ChatMessage> messages) {
            ClientV4 client = new ClientV4.Builder("{Your ApiSecretKey}").build();
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
            // System.out.println("model output:" + mapper.writeValueAsString(invokeModelApiResp));

            throw new NotImplementedException("Not implemented");
        }

}
