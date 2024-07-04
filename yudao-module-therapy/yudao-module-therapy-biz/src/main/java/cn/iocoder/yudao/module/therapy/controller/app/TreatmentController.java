package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserExtDTO;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.therapy.controller.app.vo.*;
import cn.iocoder.yudao.module.therapy.controller.vo.TreatmentInstanceVO;
import cn.iocoder.yudao.module.therapy.convert.DayitemNextStepConvert;
import cn.iocoder.yudao.module.therapy.convert.TreatmentChatHistoryConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;
import cn.iocoder.yudao.module.therapy.service.*;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "治疗方案")
@RestController
@RequestMapping("/api/treatment")
@Validated
public class TreatmentController {
    @Resource
    private TreatmentService treatmentService;

    @Resource
    private TreatmentUserProgressService treatmentUserProgressService;

    @Resource
    private TaskFlowService taskFlowService;

    @Resource
    private TreatmentChatHistoryService treatmentChatHistoryService;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private DictDataApi dictDataApi;

    @Resource
    private DayTaskEngineService dayTaskEngine;


    @PostMapping("/{code}")
    @Operation(summary = "初始化治疗流程")
//    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<TreatmentInstanceVO> initTreatmentInstance(@PathVariable("code") String code) {
        Long userId = getLoginUserId();
        Long treatmentInstanceId = treatmentService.initTreatmentInstance(userId, code);
        TreatmentInstanceVO resp = new TreatmentInstanceVO();
        resp.setId(treatmentInstanceId);
        return success(resp);
    }


    @PostMapping("/{code}/current")
    @Operation(summary = "获取用户当前治疗方案的疗程ID")
//    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<TreatmentInstanceVO> getTreatmentInstance(@PathVariable("code") String code) {
        Long userId = getLoginUserId();
        Long treatmentInstanceId = treatmentService.getCurrentTreatmentInstance(userId, code);
        TreatmentInstanceVO resp = new TreatmentInstanceVO();
        resp.setId(treatmentInstanceId);
        return success(resp);
    }


    @PostMapping("/{code}/{id}/next")
    @Operation(summary = "获取用户治疗下一项内容")
    @PreAuthenticated
    public CommonResult<TreatmentNextVO> getNext(@PathVariable("code") String code, @PathVariable("id") Long treatmentInstanceId) {
        Long userId = getLoginUserId();
        TreatmentStepItem userCurrentStep = dayTaskEngine.getCurrentStep(treatmentInstanceId);
        TreatmentStepItem stepItem;
        if(code.equals("freestyle")){
            stepItem = dayTaskEngine.getNextStepItemResult(userCurrentStep, true);
        }else{
            stepItem = dayTaskEngine.getNextStepItemResult(userCurrentStep, false);
        }
        TreatmentNextVO data = treatmentUserProgressService.convertStepItemToRespFormat(stepItem);
        treatmentUserProgressService.updateUserProgress(stepItem);
        treatmentChatHistoryService.addChatHistory(userId, treatmentInstanceId, data, true);
        return success(data);
    }

    @GetMapping("/{code}/{id}/chat-history")
    @Operation(summary = "获取用户治疗的聊天记录-主聊天页面")
    @PreAuthenticated
    public CommonResult<List<TreatmentHistoryChatMessageVO>> getChatHistory(@PathVariable("code") String code, @PathVariable("id") Long treatmentInstanceId) {
        Long userId = getLoginUserId();
        List<TreatmentChatHistoryDO> messages =  treatmentChatHistoryService.queryChatHistory(userId, treatmentInstanceId);
        return success(TreatmentChatHistoryConvert.convert(messages));
    }

    @PostMapping("/{code}/{id}/chat-history")
    @Operation(summary = "获取用户治疗的聊天记录-主聊天页面")
    @PreAuthenticated
    public CommonResult<Long> addChatHistory(@PathVariable("code") String code,
                                             @PathVariable("id") Long treatmentInstanceId,
                                             @RequestBody Map map) {
        treatmentChatHistoryService.addUserChatMessage(getLoginUserId(), treatmentInstanceId, map);
        return success(1L);
    }

    @PostMapping("/dayitem/{dayitem_instance_id}/complete")
    @Operation(summary = "完成子任务")
    @PreAuthenticated
    public CommonResult<Long> getNext(@PathVariable("dayitem_instance_id") Long dayitem_instance_id) {
        Long userId = getLoginUserId();
        treatmentService.completeDayitemInstance(userId, dayitem_instance_id);
        return success(1L);
    }

    @PostMapping("/dayitem/{dayitem_instance_id}/next")
    @Operation(summary = "获取子任务下一项内容")
    @PreAuthenticated
    public CommonResult<DayitemNextStepRespVO> subTaskGetNext(@PathVariable("dayitem_instance_id") Long dayitem_instance_id) {
        Long userId = getLoginUserId();
        Long treatmentInstanceId = 0L;
        Map data = taskFlowService.getNext(userId, treatmentInstanceId, dayitem_instance_id);
        DayitemNextStepRespVO result = DayitemNextStepConvert.convert(data);
        treatmentChatHistoryService.addTaskChatHistory(userId, treatmentInstanceId, dayitem_instance_id, result, true);
        return success(result);
    }

    @GetMapping("/dayitem/{dayitem_instance_id}/chat-history")
    @Operation(summary = "获取用户治疗的聊天记录-子任务页面")
    @PreAuthenticated
    public CommonResult<List<TreatmentHistoryChatMessageVO>> getTaskChatHistory(@PathVariable("dayitem_instance_id") Long dayitem_instance_id) {
        Long userId = getLoginUserId();
        List<TreatmentChatHistoryDO> messages =  treatmentChatHistoryService.queryTaskChatHistory(userId, dayitem_instance_id);
        return success(TreatmentChatHistoryConvert.convert(messages));
    }

    @PostMapping("/dayitem/{dayitem_instance_id}/chat-history")
    @Operation(summary = "获取用户治疗的聊天记录-子任务页面")
    @PreAuthenticated
    public CommonResult<Long> getTaskChatHistory(
            @PathVariable("dayitem_instance_id") Long dayitem_instance_id,
            @RequestBody Map map) {
        Long userId = getLoginUserId();
        treatmentChatHistoryService.addTaskUserChatMessage(userId, 0L, dayitem_instance_id, map);
        return success(1L);
    }

    @PostMapping("/{code}/taskflow/{dayitem_id}/create")
    @Operation(summary = "创建任务流程")
    @PreAuthenticated // TODO should be admin
    public CommonResult<Long> createBpmnModel(@PathVariable("code") String code, @PathVariable("dayitem_id") Long dayitem_id) {
        taskFlowService.createBpmnModel(dayitem_id);
        return success(1L);
    }

    @PostMapping("/dayitem/{dayitem_instance_id}/stepsubmit")
    @Operation(summary = "子任务step提交数据")
    @PreAuthenticated
    public CommonResult<DayitemStepSubmitRespVO> stepSubmit(@PathVariable("dayitem_instance_id") Long dayitem_instance_id,
                                                            @RequestBody DayitemStepSubmitReqVO submitReqVO) {
        Long userId = getLoginUserId();
        BaseFlow flow = taskFlowService.getTaskFlow(userId, dayitem_instance_id);
        String taskId = submitReqVO.getStep_id();
        taskFlowService.userSubmit(flow, dayitem_instance_id, taskId, submitReqVO);
        DayitemStepSubmitRespVO resp = new DayitemStepSubmitRespVO();
        DayitemStepSubmitRespVO.StepRespVO stepRespVO = new DayitemStepSubmitRespVO.StepRespVO();
        stepRespVO.setStatus("SUCCESS");
        resp.setStep_resp(stepRespVO);
        treatmentChatHistoryService.addTaskUserSubmitMessage(userId, 0L, dayitem_instance_id, submitReqVO);
        return success(resp);
    }

    @PostMapping("/clear_user_progress")
    @Operation(summary = "清空用户流程数据-临时测试用")
    @PreAuthenticated
    public CommonResult<Long> clearUserProgress() {
        // TODO REMOVE THIS FUNCTION
        Long userId = getLoginUserId();
        treatmentUserProgressService.clearUserProgress(userId);
        return success(1L);
//        truncate table hlgyy_treatment_user_progress;
//        truncate table hlgyy_treatment_dayitem_instance;
//        truncate table hlgyy_treatment_day_instance;
//        truncate table hlgyy_treatment_instance;
    }

    @GetMapping("/getFlow")
    @Operation(summary = "获取患者使用的治疗方案")
    @PreAuthenticated
    public CommonResult<String> getFlow() {
        Long userId = getLoginUserId();
        MemberUserExtDTO extDTO= memberUserApi.getUserExtInfo(userId);
        return success(dictDataApi.getDictDataLabel("flow_rule",extDTO.getTestGroup()));
    }
}
