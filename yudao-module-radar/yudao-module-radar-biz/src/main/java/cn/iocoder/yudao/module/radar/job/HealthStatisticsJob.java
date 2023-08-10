package cn.iocoder.yudao.module.radar.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.HealthStatisticsCreateReqVO;
import cn.iocoder.yudao.module.radar.controller.app.healthdata.vo.AppHealthDataReqVO;
import cn.iocoder.yudao.module.radar.controller.app.healthdata.vo.BreathAndHeartDataVO;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author whycode
 * @title: HealthStatisticsJob
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1014:15
 */
@Component
@Slf4j
@TenantJob
public class HealthStatisticsJob implements JobHandler {

    @Resource
    private HealthDataService healthDataService;

    @Resource
    private HealthStatisticsService healthStatisticsService;


    @Override
    public String execute(String param) throws Exception {

        log.info("我是雷达定时器");

        // 获取当前日期
        LocalDate now = LocalDate.now();

        if(StrUtil.isNotBlank(param)){
            now = LocalDateTimeUtil.parseDate(param, "yyyy-MM-dd");
        }
        healthDataService.healthStatistics(now);
        return "";
    }



}
