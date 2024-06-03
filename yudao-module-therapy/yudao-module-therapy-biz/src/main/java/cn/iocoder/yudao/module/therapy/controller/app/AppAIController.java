package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AutomatedThinkingRequestVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ProblemClassificationRequest;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * Author:lidongw_1
 * Date 2024/5/28
 * Description:
 **/
@Tag(name = "C端 - AI问答")
@RestController
@RequestMapping("/therapy/ai")
@Validated
public class AppAIController {

    @Resource
    private AIChatService aiChatService;

    @PostMapping("/problem-classification")
    @Operation(summary = "分析问题分类")
    public CommonResult<String> teenProblemClassification(@Valid @RequestBody ProblemClassificationRequest req) {
       String answer = aiChatService.teenProblemClassification(req.getQuestion());
       return success(answer);
    }

    @PostMapping(value = "/automated-thinking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> chat(@RequestBody AutomatedThinkingRequestVO req) {
        return aiChatService.automaticThinkingRecognition(getLoginUserId(), req.getConversationId(), req.getContent());
    }

}
