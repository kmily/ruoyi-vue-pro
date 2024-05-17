package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "治疗方案")
@RestController
@RequestMapping("/api/treatment")
@Validated
public class TreatmentController {
    @Resource
    private TreatmentService treatmentService;

    @PostMapping("/")
    @Operation(summary = "初始化治疗流程")
    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<Long> initTreatmentInstance(@RequestParam("code") String code) {
        Long userId = getLoginUserId();
        return success(treatmentService.initTreatment(userId, code));
    }
}
