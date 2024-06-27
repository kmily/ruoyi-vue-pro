package cn.iocoder.yudao.module.therapy.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.service.StatService;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_ANSWER_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
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

    @Resource
    private SurveyService surveyService;

    @GetMapping("/scheduleStat")
    @Operation(summary = "行为活动计划统计")
    @PreAuthenticated
    public CommonResult<List<ScheduleStateRespVO>> scheduleState() {
        return success(statService.StatSchedule(30, getLoginUserId()));
    }

    @GetMapping(value = "/scaleDetail")
    @Operation(summary = "量表报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<Map<String, String>> scaleDetail(@RequestParam("id") Long id) {
        return success(statService.getScaleReport(id, getLoginUserId()));
    }

    @GetMapping(value = "/scaleList")
    @Operation(summary = "量表报告列表")
    @PreAuthenticated
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
    @PreAuthenticated
    public CommonResult<Map<String, List<KeyValue>>> scaleChart() {
        Map<String, List<KeyValue>> res = statService.getScaleChart(getLoginUserId());

        return success(res);
    }

    @GetMapping(value = "/moodScoring")
    @Operation(summary = "每日心情评分拆线图数据")
    @Parameter(name = "begin", description = "开始日期:yyyy-MM-dd", required = true, example = "2024-06-01")
    @Parameter(name = "end", description = "结束日期:yyyy-MM-dd", required = true, example = "2024-06-01")
    @PreAuthenticated
    public CommonResult<List<KeyValue>> moodScoring(@RequestParam("begin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate begin
            , @RequestParam("end")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<SurveyAnswerDO> answerDOS = statService.getAnswerList(getLoginUserId(), begin, end, Arrays.asList(SurveyType.MOOD_MARK.getType()));
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

    @GetMapping(value = "/autoThoughtRecognition")
    @Operation(summary = "自动化思维识别列表")
    @PreAuthenticated
    public CommonResult<List<SurveyAnswerDO>> autoThoughtRecognition() {
        List<SurveyAnswerDO> answerDOS = statService.getAnswerList(getLoginUserId(), null, null, Arrays.asList(SurveyType.AUTO_THOUGHT_RECOGNITION.getType()));
        return success(answerDOS);
    }

    @GetMapping(value = "/getCommonDetail")
    @Operation(summary = "获取通用详情")
    @PreAuthenticated
    @Parameter(name = "id", description = "报告id", required = true, example = "1024")
    public CommonResult<SubmitSurveyReqVO> getCommonDetail(@RequestParam("id") Long id) {
        SubmitSurveyReqVO res = new SubmitSurveyReqVO();
        SurveyAnswerDO answerDO = surveyService.getAnswerDO(id);
        if (Objects.isNull(answerDO)) {
            throw exception(SURVEY_ANSWER_NOT_EXISTS);
        }
        List<AnswerDetailDO> detailDOS = surveyService.getAnswerDetailByAnswerId(id);
        res.setId(id);
        res.setSurveyType(answerDO.getSurveyType());
        res.setQstList(new ArrayList<>());
        for (AnswerDetailDO item : detailDOS) {
            AnAnswerReqVO vo = new AnAnswerReqVO();
            vo.setAnswer(item.getAnswer());
            vo.setQstCode(item.getBelongQstCode());
            res.getQstList().add(vo);
        }
        return success(res);
    }

    @GetMapping(value = "/cognizeRebuild")
    @Operation(summary = "认知重建报告列表")
    @PreAuthenticated
    public CommonResult<List<SurveyAnswerDO>> cognizeRebuild() {
        List<SurveyAnswerDO> answerDOS = statService.getAnswerList(getLoginUserId(), null, null, Arrays.asList(SurveyType.COGNIZE_REBUILD.getType()));
        return success(answerDOS);
    }

    @GetMapping("/getMoodDiary")
    @Operation(summary = "获取心情日记")
    @PreAuthenticated
    public CommonResult<List<SurveyAnswerDO>> getMoodDiary() {
        List<SurveyAnswerDO> answerDOS = statService.getAnswerList(getLoginUserId(), null, null, Arrays.asList(SurveyType.MOOD_DIARY.getType()));
        return success(answerDOS);
    }

    @GetMapping("/getStrategyCard")
    @Operation(summary = "获取对策卡")
    @PreAuthenticated
    public CommonResult<Map<String,List<JSONObject>>> getStrategyCard() {
        return success(statService.getStrategyCard(getLoginUserId()));

    }

    @GetMapping("/getReplyCard")
    @Operation(summary = "获取应对卡")
    @PreAuthenticated
    public CommonResult<List<SurveyAnswerDO>> getReplyCard() {
        List<SurveyAnswerDO> answerDOS = statService.getAnswerList(getLoginUserId(), null, null, Arrays.asList(SurveyType.REPLY_CARD.getType()));
        return success(answerDOS);
    }
}
