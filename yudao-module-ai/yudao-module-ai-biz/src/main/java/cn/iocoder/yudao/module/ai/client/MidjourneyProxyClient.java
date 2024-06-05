package cn.iocoder.yudao.module.ai.client;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.ai.client.vo.MidjourneyActionReqVO;
import cn.iocoder.yudao.module.ai.client.vo.MidjourneyImagineReqVO;
import cn.iocoder.yudao.module.ai.client.vo.MidjourneySubmitRespVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

// TODO @fan：这个写到 starter-ai 里哈。搞个 MidjourneyApi，参考 https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-openai/src/main/java/org/springframework/ai/openai/api/OpenAiApi.java 的风格写哈
/**
 * Midjourney Proxy 客户端
 *
 * @author fansili
 * @time 2024/5/30 13:58
 * @since 1.0
 */
@Component
public class MidjourneyProxyClient {

    private static final String URI_IMAGINE = "/submit/imagine";
    private static final String URI_ACTON = "/submit/action";

    @Value("${ai.midjourney-proxy.url:http://127.0.0.1:8080/mj}")
    private String url;
    @Value("${ai.midjourney-proxy.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * imagine - 根据提示词提交绘画任务
     *
     * @param imagineReqVO
     * @return
     */
    public MidjourneySubmitRespVO imagine(@Validated @NotNull MidjourneyImagineReqVO imagineReqVO) {
        // 1、发送 post 请求
        ResponseEntity<String> response = post(URI_IMAGINE, imagineReqVO);
        // 2、转换 resp
        return JsonUtils.parseObject(response.getBody(), MidjourneySubmitRespVO.class);
    }

    /**
     * action - 放大、缩小、U1、U2...
     *
     * @param actionReqVO
     */
    public MidjourneySubmitRespVO action(@Validated @NotNull MidjourneyActionReqVO actionReqVO) {
        // 1、发送 post 请求
        ResponseEntity<String> response = post(URI_ACTON, actionReqVO);
        // 2、转换 resp
        return JsonUtils.parseObject(response.getBody(), MidjourneySubmitRespVO.class);
    }

    private ResponseEntity<String> post(String uri, Object body) {
        // 1、创建 HttpHeaders 对象
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer ".concat(key));
        // 2、创建 HttpEntity 对象，将 HttpHeaders 和请求体传递给它
        HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJsonString(body), headers);
        // 3、发送 post 请求
        return restTemplate.exchange(url.concat(uri), HttpMethod.POST, requestEntity, String.class);
    }
}