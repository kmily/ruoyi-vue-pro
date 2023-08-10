package cn.iocoder.yudao.module.radar.controller.app.healthstatistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.*;
import cn.iocoder.yudao.module.radar.convert.healthstatistics.HealthStatisticsConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics.HealthStatisticsDO;
import cn.iocoder.yudao.module.radar.job.HealthStatisticsJob;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "用户 APP - 睡眠统计记录")
@RestController
@RequestMapping("/radar/health-statistics")
@Validated
public class HealthStatisticsController {

    @Resource
    private HealthStatisticsService healthStatisticsService;

    @Resource
    @Lazy
    private HealthDataService healthDataService;

    @GetMapping("/list")
    @Operation(summary = "获得睡眠统计记录列表")
    @Parameter(name = "deviceId", description = "设备编号", required = true, example = "1024")
    @Parameter(name = "startDate", description = "开始日期", required = true, example = "2023-08-07")
    @Parameter(name = "endDate", description = "结束日期", required = true, example = "2023-08-11")
    @PreAuthenticated
    public CommonResult<List<HealthStatisticsRespVO>> getHealthStatisticsList(@RequestParam("deviceId") Long deviceId,
                                                                              @RequestParam("startDate") String startDate,
                                                                              @RequestParam("endDate") String endDate) {


        healthDataService.healthStatistics(LocalDate.now());
        List<HealthStatisticsDO> list = healthStatisticsService.getHealthStatisticsList(deviceId, startDate, endDate);
        return success(HealthStatisticsConvert.INSTANCE.convertList(list));
    }



}
