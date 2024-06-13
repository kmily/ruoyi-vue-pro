package cn.iocoder.yudao.module.therapy.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AutomatedThinkingRequestVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ProblemClassificationRequest;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.StatService;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
import com.alibaba.fastjson.JSON;
import com.xingyuv.captcha.util.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @GetMapping("/scheduleStat")
    @Operation(summary = "行为活动计划统计")
    public CommonResult<List<ScheduleStateRespVO>> scheduleState() {
        return success(statService.StatSchedule(7, getLoginUserId()));
    }

    @GetMapping(value = "/scaleDetail")
    @Operation(summary = "量表报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Map<String, String>> scaleDetail(@RequestParam("id") Long id) {
        return success(statService.getScaleReport(id, getLoginUserId()));
    }

    @GetMapping(value = "/scaleList")
    @Operation(summary = "量表报告列表")
    public CommonResult<List<KeyValue<LocalDateTime, Long>>> scaleList() {
        List<SurveyAnswerDO> answerDOS = statService.getScaleList(getLoginUserId());
        List<KeyValue<LocalDateTime, Long>> res = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(answerDOS)) {
            for (SurveyAnswerDO item : answerDOS) {
                KeyValue<LocalDateTime, Long> keyValue = new KeyValue<>();
                keyValue.setKey(item.getCreateTime());
                keyValue.setValue(item.getId());
                res.add(keyValue);
            }
        }
        return success(res);
    }

    @GetMapping(value = "/scaleChart")
    @Operation(summary = "量表报告拆线图数据")
    public CommonResult<Map<String, List<KeyValue>>> scaleChart() {
        Map<String, List<KeyValue>> res = statService.getScaleChart(getLoginUserId());

        return success(res);
    }

    @GetMapping(value = "/moodScoring")
    @Operation(summary = "量表报告拆线图数据")
    @Parameter(name = "begin", description = "开始日期:yyyy-MM-dd", required = true, example = "2024-06-01")
    @Parameter(name = "end", description = "结束日期:yyyy-MM-dd", required = true, example = "2024-06-01")
    public CommonResult<List<KeyValue>> moodScoring(@RequestParam("begin") LocalDate begin
            , @RequestParam("end") LocalDate end) {
        List<SurveyAnswerDO> answerDOS = statService.getMoodScoringList(getLoginUserId(), begin, end);
        List<KeyValue> res = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(answerDOS)) {
            for (SurveyAnswerDO item : answerDOS) {
                KeyValue<LocalDateTime, Integer> keyValue = new KeyValue<>();
                keyValue.setKey(item.getCreateTime());
                keyValue.setValue((new JSONObject(item.getReprot())).getInt("score", 0));
                res.add(keyValue);
            }
        }
        return success(res);
    }

}
