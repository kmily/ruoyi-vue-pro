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
import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import com.google.common.util.concurrent.AtomicDouble;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

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


        LocalDate startDate = reqVO.getStartDate();
        if(startDate == null){
            startDate = LocalDate.now();
        }
        LocalDate yesterday = startDate.plusDays(-1L);

        // 查询昨天 20点到当前日期
        reqVO.setCreateTime(new String[]{LocalDateTimeUtil.format(yesterday, "yyyy-MM-dd") + " 08:00:00",
                                         LocalDateTimeUtil.format(startDate, "yyyy-MM-dd") + " 23:59:59"});

        List<HealthDataDO> list = healthDataService.getHealthDataList(reqVO);


        /**
         * 切分数据
         *  1、先把前一天 08 点到 20 点
         *  2、前一天20点 到 当天 8点
         *  3、当天8点 当天 20点
         */
        LocalDateTime yesterdayTwenty = yesterday.atTime(20, 0, 0);
        LocalDateTime todayEight = startDate.atTime(8, 0, 0);
        LocalDateTime todayTwenty = startDate.atTime(20, 0, 0);

        List<HealthDataDO> yesterdayTwentyList = new ArrayList<>();
        List<HealthDataDO> todayEightList = new ArrayList<>();
        List<HealthDataDO> otherList = new ArrayList<>();

        list.forEach(item -> {
            LocalDateTime createTime = item.getCreateTime();
            if(Duration.between(createTime, yesterdayTwenty).toMinutes() > 0){
                yesterdayTwentyList.add(item);
            }else {
                todayEightList.add(item);
            }
        });


        List<HealthDataDO> nightSleepList = new ArrayList<>();

        List<HealthDataDO> todaySleepLst = getSleep(todayEightList, nightSleepList, todayEight);
        if(!todaySleepLst.isEmpty()){
            LocalDateTime createTime = todaySleepLst.get(0).getCreateTime();
            // 开始睡眠时间比 昨天20点大于10分钟，视为从 8点开始，前面数据丢弃
            if(Duration.between(yesterdayTwenty, createTime).toMinutes() < 10){

            }
        }


        if(CollUtil.isEmpty(list)){
            return success(new AppHealthDataResVO());
        }

        AppHealthDataResVO resVO = new AppHealthDataResVO();
        BreathAndHeartVO heartVO = new BreathAndHeartVO();
        BreathAndHeartVO breathVO = new BreathAndHeartVO();
        resVO.setHeart(heartVO).setBreath(breathVO);

        AtomicDouble heart = new AtomicDouble();
        AtomicDouble breath = new AtomicDouble();

        List<BreathAndHeartDataVO> heartList = new ArrayList<>();
        List<BreathAndHeartDataVO> breathList = new ArrayList<>();

        heartVO.setTimeList(heartList);
        breathVO.setTimeList(breathList);

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

        double maxHeart = 0.0;
        double minHeart = Double.MAX_VALUE;
        for(HealthDataDO healthData: lowSleep) {
            LocalDateTime createTime = healthData.getCreateTime();
            Double breathFreqAverage = healthData.getBreathFreqAverage();
            Double heartFreqAverage = healthData.getHeartFreqAverage();
            heart.addAndGet(heartFreqAverage);
            breath.addAndGet(breathFreqAverage);
            String time = DateUtil.format(createTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
            heartList.add(new BreathAndHeartDataVO().setTime(time).setValue(heartFreqAverage));
            breathList.add(new BreathAndHeartDataVO().setTime(time).setValue(breathFreqAverage));
            maxHeart = Math.max(maxHeart,  heartFreqAverage);
            minHeart = Math.min(minHeart, heartFreqAverage);

        }
        HealthDataDO healthDataDO = lowSleep.get(lowSleep.size() - 1);
        heartVO.setAverage(heart.get() / lowSleep.size());
        heartVO.setCurrent(healthDataDO.getHeartFreqAverage());
        heartVO.setHighest(maxHeart).setLowest(minHeart);
        breathVO.setAverage(breath.get() / lowSleep.size());
        breathVO.setCurrent(healthDataDO.getBreathFreqAverage());
        return success(resVO);
    }

    private double average( List<HealthDataDO> lowSleep){
        return lowSleep.stream().map(HealthDataDO::getHeartFreqAverage)
                .mapToDouble(s -> s).summaryStatistics().getAverage();

    }


    private List<HealthDataDO> getSleep(List<HealthDataDO> list,  List<HealthDataDO> sleepList,  LocalDateTime todayEight){
        List<HealthDataDO> lowSleep = new ArrayList<>();
        List<HealthDataDO> wakeList = new ArrayList<>();
        LocalDateTime sleepStart = null;
        LocalDateTime wakeTime = null;

        // 睡眠段
        List<List<HealthDataDO>> sleepPartList = new ArrayList<>();
        // 清醒时间段
        List<List<HealthDataDO>> wakePartList = new ArrayList<>();


        boolean isSleep = false;
        boolean isWake = false;

        for(HealthDataDO healthDataDO: list){
            Double heartFreqAverage = healthDataDO.getHeartFreqAverage();
            Integer hasPeople = healthDataDO.getHasPeople();
            Integer hasMove = healthDataDO.getHasMove();
            LocalDateTime createTime = healthDataDO.getCreateTime();

            if(Objects.equals(1, hasPeople) && SLEEP.compareTo(heartFreqAverage) > 0){
                // 此处表示心率低于睡眠检查时间
                if(sleepStart == null){
                    sleepStart = createTime;
                }else{
                    // 如果时间小于等于 1 分钟表明是连续性的
                    if(Duration.between(createTime, sleepStart).toMinutes() <= 1) {
                        sleepStart = createTime;
                    }else{
                        lowSleep.clear();
                    }
                    lowSleep.add(healthDataDO);
                }
            }else{
                // 低心率大于 10 表明睡眠了
                if(lowSleep.size() > 10){

                    if(isWake){
                        wakePartList.add(new ArrayList<>(wakeList));
                        wakeList.clear();
                        isWake = false;
                    }

                    // 设置已经睡眠
                    isSleep = true;
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
                                wakeTime = null;
                            }
                        }
                        // 连续 3分钟 说明醒了
                        if(wakeList.size() >= 3){
                            //break;
                            sleepPartList.add(new ArrayList<>(lowSleep));
                            sleepStart = null;
                            lowSleep.clear();
                            isWake = true;
                        }
                    }else{
                        lowSleep.addAll(wakeList);
                        lowSleep.add(healthDataDO);
                        wakeList.clear();
                    }
                }else{

                    if(isSleep){
                        // 夜晚睡眠中间 醒来
                        wakeList.add(healthDataDO);
                    }
                    lowSleep.clear();
                    sleepStart = null;
                }
            }
        }

        if(lowSleep.size() > 0){
            sleepPartList.add(new ArrayList<>(lowSleep));
        }

        if(wakeList.size() > 0){
            wakePartList.add( new ArrayList<>(wakeList));
        }

        return  lowSleep;
    }


}
