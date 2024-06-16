package cn.iocoder.yudao.module.therapy.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 活动计算表评分
 */
@Component
@Slf4j
public class ScheduleScoreJob implements JobHandler {
    @Override
    public String execute(String param) throws Exception {
        return null;
    }
}
