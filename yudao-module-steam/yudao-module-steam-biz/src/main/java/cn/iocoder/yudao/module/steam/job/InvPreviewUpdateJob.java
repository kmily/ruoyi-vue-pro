package cn.iocoder.yudao.module.steam.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新InvPreView表相关数据
 */
@Slf4j
@Component
public class InvPreviewUpdateJob implements JobHandler {

    private InvPreviewExtService invPreviewExtService;

    @Autowired
    public void setInvPreviewExtService(InvPreviewExtService invPreviewExtService) {
        this.invPreviewExtService = invPreviewExtService;
    }

/*    @TenantJob*/
    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> invPreviewExtService.updateIvnFlag());
        return String.format("执行更新成功 %s 个", execute);
    }
}
