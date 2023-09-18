package cn.iocoder.yudao.module.radar.service.healthdata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.radar.controller.app.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.HealthStatisticsCreateReqVO;
import cn.iocoder.yudao.module.radar.service.healthstatistics.HealthStatisticsService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.AtomicDouble;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
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
            LocalDateTime todayEight = date.atTime(8, 0,0);
            List<HealthDataDO> lowSleep = new ArrayList<>();
            AppNightSleepVO appNightSleepVO = getSleep(healthDataDOList, lowSleep, todayEight);
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
                    .setStartTime(LocalDateTimeUtil.format(date, "yyyy-MM-dd"))
                    .setDeviceId(lowSleep.get(0).getDeviceId())
                    .setHeartSilent(silentSize != 0? silent/silentSize: 0.0)
                    .setBreathMaxValue(maxBreath)
                    .setSleepStart(appNightSleepVO.getStart())
                    .setSleepEnd(appNightSleepVO.getEnd())
                    .setSleepTotalTime(appNightSleepVO.getTotalTime())
                    .setSleepDayTime(appNightSleepVO.getDayTime())
                    .setDayStart(appNightSleepVO.getDayStart())
                    .setDayEnd(appNightSleepVO.getDayEnd());
            //createReqVOS.add(createReqVO);

            List<AppSleepOrWeekVO> orWeekVOS = appNightSleepVO.getSleepOrWeekVOS();
            long[] sleepData = {0L, 0L, 0L};
            if(CollUtil.isNotEmpty(orWeekVOS)){
                orWeekVOS.forEach(week -> {
                    Integer type = week.getType();
                    if(type == 0){
                        //当前状态 0-无人，1-醒着，2-睡眠
                        LocalDateTime start1 = week.getStart();
                        LocalDateTime end1 = week.getEnd();
                        long minutes = Duration.between(start1, end1).toMinutes();
                        switch (week.getStatus()){
                            case 0:
                                sleepData[2] += minutes;
                                break;
                            case 1:
                                sleepData[1] += minutes;
                                break;
                            case 2:
                                sleepData[0] += minutes;
                                break;
                        }
                    }
                });
            }
            createReqVO.setSleepData(JSON.toJSONString(sleepData));
            healthStatisticsService.createHealthStatistics(createReqVO);
        });
    }

    @Override
    public AppHealthDataResVO queryTodaySleep(AppHealthDataReqVO reqVO) {

        List<HealthDataDO> list = this.getHealthDataList(reqVO);

        LocalDate startDate = reqVO.getStartDate();
        if(startDate == null){
            startDate = LocalDate.now();
        }
        LocalDate yesterday = startDate.plusDays(-1L);

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
                // 存储
                yesterdayTwentyList.add(item);
            }else {
                todayEightList.add(item);
            }
        });


        List<HealthDataDO> nightSleepList = new ArrayList<>();

        AppNightSleepVO appNightSleepVO = getSleep(todayEightList, nightSleepList, todayEight);
        /*if(appNightSleepVO.isEmpty()){
            LocalDateTime createTime = todaySleepLst.get(0).getCreateTime();
            // 开始睡眠时间比 昨天20点大于10分钟，视为从 8点开始，前面数据丢弃
            if(Duration.between(yesterdayTwenty, createTime).toMinutes() < 10){

            }
        }*/

        if(CollUtil.isEmpty(list)){
            return new AppHealthDataResVO();
        }

        AppHealthDataResVO resVO = new AppHealthDataResVO();
        BreathAndHeartVO heartVO = new BreathAndHeartVO();
        BreathAndHeartVO breathVO = new BreathAndHeartVO();
        resVO.setHeart(heartVO).setBreath(breathVO)
                .setSleepVO(appNightSleepVO);

        AtomicDouble heart = new AtomicDouble();
        AtomicDouble breath = new AtomicDouble();

        List<BreathAndHeartDataVO> heartList = new ArrayList<>();
        List<BreathAndHeartDataVO> breathList = new ArrayList<>();

        heartVO.setTimeList(heartList);
        breathVO.setTimeList(breathList);

        List<HealthDataDO> lowSleep = new ArrayList<>(nightSleepList);

        double maxHeart = 0.0;
        double minHeart = Double.MAX_VALUE;
        double maxBreath = 0.0, minBreath = Double.MAX_VALUE;
        for(HealthDataDO healthData: lowSleep) {
            LocalDateTime createTime = healthData.getCreateTime();
            Double breathFreqAverage = healthData.getBreathFreqAverage();
            Double heartFreqAverage = healthData.getHeartFreqAverage();
            heart.addAndGet(heartFreqAverage);
            breath.addAndGet(breathFreqAverage);
            heartList.add(new BreathAndHeartDataVO().setTime(createTime).setValue(heartFreqAverage));
            breathList.add(new BreathAndHeartDataVO().setTime(createTime).setValue(breathFreqAverage));
            maxHeart = Math.max(maxHeart,  heartFreqAverage);
            minHeart = Math.min(minHeart, heartFreqAverage);

            maxBreath = Math.max(maxBreath, breathFreqAverage);
            minBreath = Math.min(minBreath, breathFreqAverage);

        }
        if(lowSleep.size() > 0){
            HealthDataDO healthDataDO = lowSleep.get(lowSleep.size() - 1);
            heartVO.setAverage(heart.get() / lowSleep.size());
            heartVO.setCurrent(healthDataDO.getHeartFreqAverage());
            heartVO.setHighest(maxHeart).setLowest(minHeart);
            breathVO.setAverage(breath.get() / lowSleep.size());
            breathVO.setCurrent(healthDataDO.getBreathFreqAverage());
            breathVO.setHighest(maxBreath).setLowest(maxBreath);
        }
        return  resVO;
    }

    @Override
    public HealthDataDO selectAvg(HealthDataPageReqVO reqVO) {

        List<HealthDataDO> dataDOS = this.healthDataMapper.selectList(new QueryWrapper<HealthDataDO>()
                .select("breath_freq_average as breathFreqAverage", " heart_freq_average as heartFreqAverage")
                .eq("`device_id`", reqVO.getDeviceId())
                .between("`create_time`", reqVO.getCreateTime()[0], reqVO.getCreateTime()[1])
                .orderByDesc("`id`").last(" LIMIT 1"));

        return dataDOS.isEmpty()? null: dataDOS.get(0);
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


    /**
     * 处理用户睡眠
     * @param list
     * @param sleepList
     * @param todayEight
     * @return
     */
    private AppNightSleepVO getSleep(List<HealthDataDO> list, List<HealthDataDO> sleepList, LocalDateTime todayEight){

        long start1 = System.currentTimeMillis();

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
            String date = LocalDateTimeUtil.format(createTime, "yyyy-MM-dd HH:mm:ss");
            if(Objects.equals(1, hasPeople) && SLEEP.compareTo(heartFreqAverage) > 0){
                // 此处表示心率低于睡眠检查时间
                if(sleepStart == null){
                    sleepStart = createTime;
                }else{
                    // 如果时间小于等于 1 分钟表明是连续性的
                    if(Duration.between(sleepStart, createTime).toMinutes() <= 1) {
                        sleepStart = createTime;
                    }else{
                        lowSleep.clear();
                    }
                    lowSleep.add(healthDataDO);
                    if(isSleep){
                        wakeList.clear();
                    }
                }
            }else{
                // 低心率大于 10 表明睡眠了
                if(lowSleep.size() > 10){
                    sleepStart = createTime;
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

        long end1 = System.currentTimeMillis();

        AppNightSleepVO appNightSleepVO = new AppNightSleepVO();

        if(sleepPartList.size() > 0){
            LocalDateTime start = null;
            LocalDateTime end = null;
            LocalDateTime dayStart = null;
            LocalDateTime dayEnd = null;
            if(sleepPartList.size() == 1){
                List<HealthDataDO> dos = sleepPartList.get(0);
                start = dos.get(0).getCreateTime();
                end = dos.get(dos.size() - 1).getCreateTime();
            }else{
                for (int i = 0, len = sleepPartList.size(); i < len; i++){
                    List<HealthDataDO> dos = sleepPartList.get(i);
                    HealthDataDO dataDO = dos.get(0);
                    if(i == 0){
                        start = dataDO.getCreateTime();
                    }else if(Duration.between(todayEight, dataDO.getCreateTime()).toMinutes() > 0){
                        end = sleepPartList.get(i - 1).get(sleepPartList.get(i - 1).size() - 1).getCreateTime();
                    }else if(end == null && i == len - 1){
                        end = dos.get(dos.size() - 1).getCreateTime();
                    }
                }
            }


            List<AppSleepOrWeekVO> sleepOrWeekVOS = new ArrayList<>();

            for (int i = 0, len = sleepPartList.size(); i < len; i++){
                List<HealthDataDO> dos = sleepPartList.get(i);
                LocalDateTime sleepS = dos.get(0).getCreateTime();
                LocalDateTime sleepE = dos.get(dos.size() - 1).getCreateTime();
                AppSleepOrWeekVO weekVO = new AppSleepOrWeekVO();
                weekVO.setStart(sleepS).setEnd(sleepE).setStatus(2);
                if(Duration.between(todayEight, sleepS).toMinutes() > 0){
                    weekVO.setType(1);
                }else{
                    weekVO.setType(0);
                    if(i > 0){
                        LocalDateTime weekS =  sleepPartList.get(i - 1).get(sleepPartList.get(i - 1).size() - 1).getCreateTime();
                        sleepOrWeekVOS.add(new AppSleepOrWeekVO().setType(0).setStart(weekS).setEnd(sleepS).setStatus(1));
                    }
                }
                sleepOrWeekVOS.add(weekVO);
            }
            long totalTime = 0, dayTime = 0;
            for (AppSleepOrWeekVO week: sleepOrWeekVOS){
                if(2 == week.getStatus()){
                    totalTime += Duration.between(week.getStart(), week.getEnd()).toMinutes();
                    if(1 == week.getType()){
                        if(dayStart == null){
                            dayStart = week.getStart();
                        }
                        dayEnd = week.getEnd();
                        dayTime += Duration.between(week.getStart(), week.getEnd()).toMinutes();
                    }
                }
            }
            LocalDateTime finalStart = start;
            LocalDateTime finalEnd = end;
            List<HealthDataDO> collect = list.stream().filter(item -> {
                return Duration.between(finalStart, item.getCreateTime()).toMinutes() > 0
                        && Duration.between(item.getCreateTime(), finalEnd).toMinutes() > 0;
            }).collect(Collectors.toList());

            sleepList.clear();
            sleepList.addAll(collect);

            appNightSleepVO.setEnd(end)
                    .setStart(start)
                    .setTotalTime(totalTime)
                    .setDayTime(dayTime)
                    .setDayStart(dayStart)
                    .setDayEnd( dayEnd)
                    .setSleepOrWeekVOS(sleepOrWeekVOS);
        }
        return  appNightSleepVO;
    }



}
