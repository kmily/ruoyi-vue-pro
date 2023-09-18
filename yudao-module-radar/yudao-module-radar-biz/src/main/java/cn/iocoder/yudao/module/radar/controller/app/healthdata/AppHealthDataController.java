package cn.iocoder.yudao.module.radar.controller.app.healthdata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.controller.app.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo.AppLineRuleDataInfoVO;
import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.AtomicDouble;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Slf4j
@Tag(name = "用户APP - 体征数据")
@RestController
@RequestMapping("/radar/health-data")
@Validated
public class AppHealthDataController {

    @Resource
    private HealthDataService healthDataService;

    @Resource
    private HealthStatisticsService healthStatisticsService;


    private final Double SLEEP = 76.0;

    @GetMapping("/list")
    @Operation(summary = "获得心率和呼吸数据")
    @PreAuthenticated
    public CommonResult<AppHealthDataResVO> getHealthDataList(AppHealthDataReqVO reqVO) {

        long start = System.currentTimeMillis();

        LocalDate startDate = reqVO.getStartDate();
        if(startDate == null){
            startDate = LocalDate.now();
        }
        LocalDate yesterday = startDate.plusDays(-1L);

        // 查询昨天 20点到当前日期
        reqVO.setCreateTime(new String[]{LocalDateTimeUtil.format(yesterday, "yyyy-MM-dd") + " 08:00:00",
                                         LocalDateTimeUtil.format(startDate, "yyyy-MM-dd") + " 23:59:59"});

        AppHealthDataResVO resVO = healthDataService.queryTodaySleep(reqVO);

        return success(resVO);
    }


    @GetMapping("/log")
    @Operation(summary = "获得心率和呼吸数据")
    @PreAuthenticated
    public CommonResult<AppHealthDataLogResVO> queryHealthDataDetail(AppHealthDataLogReqVO logReqVO){

        HealthDataPageReqVO reqVO = new HealthDataPageReqVO();
        LocalDate queryDate = logReqVO.getQueryDate();
        reqVO.setDeviceId(logReqVO.getDeviceId())
                .setCreateTime(new LocalDateTime[]{queryDate.atTime(0,0,0), queryDate.atTime(23,59,59)});

        reqVO.setPageNo(logReqVO.getPageNo()).setPageSize(logReqVO.getPageSize());

        HealthDataDO healthDataDO = this.healthDataService.selectAvg(reqVO);
        
        PageResult<HealthDataDO> healthDataPage = this.healthDataService.getHealthDataPage(reqVO);

        List<AppHealthDataLogItemVO> itemVOS = healthDataPage.getList().stream().map(item -> {
            return new AppHealthDataLogItemVO()
                    .setHeart(item.getHeartFreqAverage().longValue())
                    .setBreath(item.getBreathFreqAverage().longValue())
                    .setTime(item.getCreateTime())
                    .setHasPeople(item.getHasPeople());
        }).collect(Collectors.toList());
        PageResult<AppHealthDataLogItemVO> result = new PageResult<>();
        result.setList(itemVOS).setTotal(healthDataPage.getTotal());
        long heart = 0L, breath = 0L;
        if(healthDataDO != null){
             breath = healthDataDO.getBreathFreqAverage().longValue();
             heart = healthDataDO.getHeartFreqAverage().longValue();
        }
        return success(new AppHealthDataLogResVO().setPageResult(result)
                .setHeart(heart).setBreath(breath));
    }





}
