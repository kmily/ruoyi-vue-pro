package cn.iocoder.yudao.module.radar.service.healthdata;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.module.radar.controller.app.healthdata.vo.AppHealthDataReqVO;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.HealthStatisticsCreateReqVO;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.healthdata.HealthDataMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
//import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 体征数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HealthDataServiceImpl implements HealthDataService {
    private final Double SLEEP = 70.0;
    @Resource
    private HealthDataMapper healthDataMapper;

    @Resource
    @Lazy
    private HealthStatisticsService healthStatisticsService;

    @Override
    public Long createHealthData(HealthDataCreateReqVO createReqVO) {
        // 插入
        HealthDataDO healthData = HealthDataConvert.INSTANCE.convert(createReqVO);
        healthDataMapper.insert(healthData);
        // 返回
        return healthData.getId();
    }

    @Override
    public void updateHealthData(HealthDataUpdateReqVO updateReqVO) {
        // 校验存在
        validateHealthDataExists(updateReqVO.getId());
        // 更新
        HealthDataDO updateObj = HealthDataConvert.INSTANCE.convert(updateReqVO);
        healthDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteHealthData(Long id) {
        // 校验存在
        validateHealthDataExists(id);
        // 删除
        healthDataMapper.deleteById(id);
    }

    private void validateHealthDataExists(Long id) {
        if (healthDataMapper.selectById(id) == null) {
            //throw exception(HEALTH_DATA_NOT_EXISTS);
        }
    }

    @Override
    public HealthDataDO getHealthData(Long id) {
        return healthDataMapper.selectById(id);
    }

    @Override
    public List<HealthDataDO> getHealthDataList(Collection<Long> ids) {
        return healthDataMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HealthDataDO> getHealthDataPage(HealthDataPageReqVO pageReqVO) {
        return healthDataMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HealthDataDO> getHealthDataList(HealthDataExportReqVO exportReqVO) {
        return healthDataMapper.selectList(exportReqVO);
    }

    @Override
    public List<HealthDataDO> getHealthDataList(AppHealthDataReqVO reqVO) {
        return healthDataMapper.selectList(reqVO);
    }

    @Override
    public void healthStatistics(LocalDate date) {
        LocalDate yesterday = date.plusDays( -1 );
        String start = LocalDateTimeUtil.format(yesterday, "yyyy-MM-dd") + " 20:00:00";
        String end = LocalDateTimeUtil.format(date, "yyyy-MM-dd") + " 20:00:00";
        AppHealthDataReqVO reqVO = new AppHealthDataReqVO();
        reqVO.setCreateTime(new String[]{start,end});
        List<HealthDataDO> list = this.getHealthDataList(reqVO);
        if(list.isEmpty()){
            return ;
        }
        Map<Long, List<HealthDataDO>> deviceDataMap = list.stream().collect(Collectors.groupingBy(HealthDataDO::getDeviceId));

        deviceDataMap.values().parallelStream().forEach((healthDataDOList) -> {
            List<HealthDataDO> lowSleep = sleep(healthDataDOList);
            if(lowSleep.isEmpty()){
                return;
            }
            double maxHeart = 0.0;
            double minHeart = Double.MAX_VALUE;
            double silent = 0.0;
            double silentSize = 0;

            double maxBreath = 0.0;
            double minBreath = Double.MAX_VALUE;

            for(HealthDataDO healthData: lowSleep) {
                Double breathFreqAverage = healthData.getBreathFreqAverage();
                Double heartFreqAverage = healthData.getHeartFreqAverage();
                maxHeart = Math.max(maxHeart,  heartFreqAverage);
                minHeart = Math.min(minHeart, heartFreqAverage);

                maxBreath = Math.max(maxBreath,  breathFreqAverage);
                minBreath = Math.min(minBreath, breathFreqAverage);
                if(healthData.getHasMove() == 0){
                    silent += heartFreqAverage;
                    silentSize += 1;
                }
            }

            double average = lowSleep.stream().map(HealthDataDO::getBreathFreqAverage)
                    .mapToDouble(s -> s).summaryStatistics().getAverage();

            HealthStatisticsCreateReqVO createReqVO = new HealthStatisticsCreateReqVO();
            createReqVO.setHeartAverage(average(lowSleep))
                    .setBreathAverage(average)
                    .setBreathMinValue(minBreath)
                    .setHeartMaxValue(maxHeart)
                    .setHeartMinValue(minHeart)
                    .setStartTime(LocalDateTimeUtil.format(lowSleep.get(0).getCreateTime(), "yyyy-MM-dd"))
                    .setDeviceId(lowSleep.get(0).getDeviceId())
                    .setHeartSilent(silentSize != 0? silent/silentSize: 0.0)
                    .setBreathMaxValue(maxBreath);
            //createReqVOS.add(createReqVO);

            healthStatisticsService.createHealthStatistics(createReqVO);
        });
    }

    public List<HealthDataDO> sleep(List<HealthDataDO> list){
        List<HealthDataDO> lowSleep = new ArrayList<>();
        List<HealthDataDO> wakeList = new ArrayList<>();
        LocalDateTime sleepStart = null;
        LocalDateTime wakeTime = null;

        for(HealthDataDO healthDataDO: list){
            Double heartFreqAverage = healthDataDO.getHeartFreqAverage();
            Integer hasPeople = healthDataDO.getHasPeople();
            LocalDateTime createTime = healthDataDO.getCreateTime();
            if(!Objects.equals(0, hasPeople) && SLEEP.compareTo(heartFreqAverage) > 0){
                // 此处表示心率低于睡眠检查时间
                if(sleepStart == null){
                    sleepStart = createTime;
                }else{
                    // 如果时间小于等于 1 分钟表明是连续性的
                    if(Duration.between(createTime, sleepStart).toMinutes() <= 1) {
                        sleepStart = createTime;
                        lowSleep.add(healthDataDO);
                    }else{
                        lowSleep.clear();
                        lowSleep.add(healthDataDO);
                    }
                }
            }else{
                // 低心率大于 10 表明睡眠了
                if(lowSleep.size() > 10){
                    double average = average(lowSleep);
                    if(hasPeople == 0 || (heartFreqAverage - average) >= 0.2 * heartFreqAverage){
                        wakeList.add(healthDataDO);
                        if(wakeTime == null){
                            wakeTime = createTime;
                        }else{
                            if(Duration.between(createTime, wakeTime).toMinutes() <= 1) {
                                wakeTime = createTime;
                            }else{
                                lowSleep.addAll(wakeList);
                                wakeList.clear();
                            }
                        }
                        if(wakeList.size() >= 3){
                            break;
                        }
                    }else{
                        lowSleep.addAll(wakeList);
                        lowSleep.add(healthDataDO);
                        wakeList.clear();
                    }
                }else{
                    lowSleep.clear();
                    sleepStart = null;
                }
            }
        }

        return lowSleep;
    }


    private double average( List<HealthDataDO> lowSleep){
        return lowSleep.stream().map(HealthDataDO::getHeartFreqAverage)
                .mapToDouble(s -> s).summaryStatistics().getAverage();

    }
}
