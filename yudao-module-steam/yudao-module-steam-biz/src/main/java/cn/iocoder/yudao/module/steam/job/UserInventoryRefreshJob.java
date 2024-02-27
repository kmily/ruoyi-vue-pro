package cn.iocoder.yudao.module.steam.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 定时任务-刷新用户库存,查询用户表 steam_bind_user 获取 steamId
 * CSGO固定 appid 为 730
 */
@Component
@Slf4j
//@TenantJob
public class UserInventoryRefreshJob implements JobHandler {

    @Resource
    private SteamInvService steamInvService;

    @Resource
    private BindUserMapper bindUserMapper;

    @Override
    public String execute(String param) {

        // 查询用户表 steam_bind_user 的 steamId
        List<Long> bindUserIds = bindUserMapper.selectList()
                .stream()
                .distinct()
                .map(BindUserDO::getUserId)
                .collect(Collectors.toList());
        if (!bindUserIds.isEmpty()){
            for (Long bindUserId : bindUserIds){
                steamInvService.fetchInventory(bindUserId,"730");
            }
        } else {
            return "定时任务-用户库存查询为空";
        }
        return "定时任务-更新库存成功";
    }
}
