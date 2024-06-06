package cn.iocoder.yudao.module.therapy.controller.admin;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.controller.vo.TreatmentInstanceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "后台管理-治疗方案-标量")
@RestController
@RequestMapping("/admin/treatment/statistics/data")
@Validated
public class TreatmentStatisticsDataController {

    @GetMapping("/{treatment_instance_id}")
    @Operation(summary = "治疗进度数据")
//    @Parameter(name="code", description = "治疗流程Code", required = true, example = "main")
//    @PreAuthorize("@ss.hasPermission('bpm:category:create')")
    @PreAuthenticated
    public CommonResult<TreatmentProgressRespVO> initTreatmentInstance(@PathVariable("treatment_instance_id") String treatment_instance_id) {
        TreatmentProgressRespVO treatmentProgressRespVO = new TreatmentProgressRespVO();
        return success(treatmentProgressRespVO);
    }

}
