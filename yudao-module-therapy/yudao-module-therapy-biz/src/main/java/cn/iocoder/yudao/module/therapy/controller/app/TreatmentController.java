package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitRespVO;
import cn.iocoder.yudao.module.therapy.controller.vo.TreatmentInstanceVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentNextVO;
import cn.iocoder.yudao.module.therapy.convert.DayitemNextStepConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.service.TaskFlowService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.service.TreatmentUserProgressService;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
        TreatmentStepItem userCurrentStep = treatmentUserProgressService.getTreatmentUserProgress(userId, treatmentInstanceId);
        TreatmentStepItem stepItem = treatmentService.getNext(userCurrentStep);
        TreatmentNextVO data = treatmentUserProgressService.convertStepItemToRespFormat(stepItem);
        treatmentUserProgressService.updateUserProgress(stepItem);
        return success(data);
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
        return success(DayitemNextStepConvert.convert(data));
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
        return success(resp);
    }

    @PostMapping("/clear_user_progress")
    @Operation(summary = "清空用户流程数据-临时测试用")
    @PreAuthenticated
    public CommonResult<Long> clearUserProgress() {
        Long userId = getLoginUserId();
        treatmentUserProgressService.clearUserProgress(userId);
        return success(1L);
//        truncate table hlgyy_treatment_user_progress;
//        truncate table hlgyy_treatment_dayitem_instance;
//        truncate table hlgyy_treatment_day_instance;
//        truncate table hlgyy_treatment_instance;
    }


}
