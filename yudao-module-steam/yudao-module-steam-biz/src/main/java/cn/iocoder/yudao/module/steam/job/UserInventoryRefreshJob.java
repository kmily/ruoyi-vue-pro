package cn.iocoder.yudao.module.steam.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
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

        String execute = TenantUtils.execute(1l, () -> {
            List<Long> bindUserIds = bindUserMapper.selectList()
                .stream()
                .distinct()
                .map(BindUserDO::getUserId)
                .collect(Collectors.toList());
            if (!bindUserIds.isEmpty()){
                for (Long bindUserId : bindUserIds){
                    steamInvService.fetchInventory(bindUserId,"730");
                }
            }

            return "完成";
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
