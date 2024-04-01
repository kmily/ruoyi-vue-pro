package cn.iocoder.yudao.module.steam.job.uu;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodeityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务----模板ID
 */
//@Slf4j
//@Component
//public class UUInvUpdateJob implements JobHandler {
//
//    @Resource
//    private ApiUUCommodeityService apiUUCommodeityService;
//
//
////    @Override
////    public String execute(String param) {
////        // 定时更新商品模板
////        Integer execute = TenantUtils.execute(1L, () -> {
////            apiUUCommodeityService.
////
////
////        });
////        return String.format("执行更新成功 %s 个", execute);
////    }
//}
