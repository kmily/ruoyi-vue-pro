package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.module.therapy.controller.app.vo.GetNextRespVO;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.service.TreatmentUserProgressService;
import cn.iocoder.yudao.module.therapy.service.common.StepResp;
import cn.iocoder.yudao.module.therapy.service.common.TreatmentStepItem;
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

    @PostMapping("/{code}")
    @Operation(summary = "初始化治疗流程")
//    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<Long> initTreatmentInstance(@PathVariable("code") String code) {
        Long userId = getLoginUserId();
        return success(treatmentService.initTreatmentInstance(userId, code));
    }


    @PostMapping("/{code}/current")
    @Operation(summary = "获取用户当前治疗方案的疗程ID")
//    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<Long> getTreatmentInstance(@PathVariable("code") String code) {
        Long userId = getLoginUserId();
        return success(treatmentService.getCurrentTreatmentInstance(userId, code));
    }


    @PostMapping("/{code}/{id}/next")
    @Operation(summary = "获取用户治疗下一项内容")
    @PreAuthenticated
    public CommonResult<GetNextRespVO> getNext(@PathVariable("code") String code, @PathVariable("id") Long treatmentInstanceId) {
        Long userId = getLoginUserId();
        TreatmentStepItem userCurrentStep = treatmentUserProgressService.getTreatmentUserProgress(userId, treatmentInstanceId);
        StepResp stepResp = treatmentService.getNext(userCurrentStep);
        GetNextRespVO getNextRespVO = new GetNextRespVO();
        getNextRespVO.setData(stepResp.getData());
        return success(getNextRespVO);
    }

    @PostMapping("/{code}/{id}/complete/{dayitem_instance_id}")
    @Operation(summary = "完成某一项任务")
    @PreAuthenticated
    public CommonResult<Long> getNext(@PathVariable("code") String code, @PathVariable("id") Long treatmentInstanceId, @PathVariable("dayitem_instance_id") Long dayitem_instance_id) {
        Long userId = getLoginUserId();
        treatmentService.completeDayitemInstance(userId, treatmentInstanceId, dayitem_instance_id);
        return success(1L);
    }


}
