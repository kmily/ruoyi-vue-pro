package cn.iocoder.yudao.module.therapy.controller.admin;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import cn.iocoder.yudao.module.therapy.service.StatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "后台管理-治疗方案-标量")
@RestController
@RequestMapping("/therapy/data")
@Validated
public class TreatmentStatisticsDataController {
    @Resource
    private StatService statService;

    @Resource
    private TreatmentStatisticsDataService treatmentStatisticsDataService;

    @GetMapping("/{treatment_instance_id}")
    @Operation(summary = "治疗进度数据")
    @PreAuthenticated
    public CommonResult<TreatmentProgressRespVO> initTreatmentInstance(@PathVariable("treatment_instance_id") Long treatment_instance_id) {
        TreatmentProgressRespVO treatmentProgressRespVO = treatmentStatisticsDataService.getTreatmentProgress(treatment_instance_id);
        return success(treatmentProgressRespVO);
    }

    @GetMapping("/useToolsTotal")
    @Operation(summary = "使用工具总次数")
    @Parameter(name = "userId", description = "患者id", required = true, example = "1024")
    public CommonResult<Long> useToolsTotal(@RequestParam("userId") Long userId) {
        return success(statService.useToolsTotal(userId));
    }

    @GetMapping("/useToolsNum")
    @Operation(summary = "使用每种工具次数")
    @Parameter(name = "userId", description = "患者id", required = true, example = "1024")
    public CommonResult<Map<Long,Long>> useToolsNum(@RequestParam("userId") Long userId) {
        return success(statService.useToolsNum(userId));
    }


    @GetMapping("/userInTreatment")
    @Operation(summary = "治疗中的用户数量")
    @Parameter(name = "startDate", description = "起始日期（包含）", required = true, example = "2015-03-02")
    public CommonResult<Map<String, Integer>> userInUse(@RequestParam("startDate") String startDate) {
        Map<String, Integer> result = new HashMap<>();
        result.put("finishedEvaluationCount", 1); //完成初步评估的用户人次
        result.put("inTreatmentCount", 2); // 正在治疗的用户人次
        result.put("finishedTreatmentCount", 3); // 已经完成的用户人次
        return success(result);
    }
}
