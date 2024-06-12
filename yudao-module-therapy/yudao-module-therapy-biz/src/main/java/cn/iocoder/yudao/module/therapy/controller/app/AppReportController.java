package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AutomatedThinkingRequestVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ProblemClassificationRequest;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.StatService;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
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

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * Author:lidongw_1
 * Date 2024/5/28
 * Description:
 **/
@Tag(name = "C端 - 数据及报告")
@RestController
@RequestMapping("/therapy/report")
@Validated
public class AppReportController {

    @Resource
    private StatService statService;

    @PostMapping("/scheduleState")
    @Operation(summary = "行为活动计划统计")
    public CommonResult<List<ScheduleStateRespVO>> scheduleState() {
       return success(statService.StatSchedule(7,getLoginUserId()));
    }

//    @PostMapping(value = "/automated-thinking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<SSEMsgDTO> chat(@Valid @RequestBody AutomatedThinkingRequestVO req) {
//
//        Long loginUserId = getLoginUserId();
//        loginUserId = NumberUtils.toLong(loginUserId,Long.valueOf(req.getConversationId().hashCode()));
//        return aiChatService.automaticThinkingRecognition(loginUserId, req.getConversationId(), req.getContent());
//    }

}
