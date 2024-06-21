package cn.iocoder.yudao.module.therapy.controller.app;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "治疗方案-标量")
@RestController
@RequestMapping("/app/treatment/statistics/data")
@Validated
public class AppTreatmentStatisticsDataController {

    @Resource
    private TreatmentStatisticsDataService treatmentStatisticsDataService;

    @GetMapping("/{treatment_instance_id}")
    @Operation(summary = "治疗进度数据")
    @PreAuthenticated
    public CommonResult<TreatmentProgressRespVO> initTreatmentInstance(@PathVariable("treatment_instance_id") Long treatment_instance_id) {
        TreatmentProgressRespVO treatmentProgressRespVO = treatmentStatisticsDataService.getTreatmentProgress(treatment_instance_id);
        return success(treatmentProgressRespVO);
    }

}
