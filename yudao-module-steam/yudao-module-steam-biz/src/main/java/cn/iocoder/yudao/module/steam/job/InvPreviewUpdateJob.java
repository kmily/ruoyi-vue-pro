package cn.iocoder.yudao.module.steam.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新InvPreView表相关数据
 */
@Slf4j
@Component
public class InvPreviewUpdateJob implements JobHandler {
    @Autowired
    private InvPreviewExtService invPreviewExtService;
    @Override
//    @TenantJob
    public String execute(String param) throws Exception {

        Integer execute = TenantUtils.execute(1l, () -> {
            return invPreviewExtService.updateIvnFlag();
        });
        return String.format("执行更新成功 %s 个", execute);
    }
}
