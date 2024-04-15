package cn.iocoder.yudao.module.steam.job.selling;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("otherSellingInsertC5Job")
public class OtherSellingInsertC5Job implements JobHandler {

    @Resource
    private IOInvUpdateService ioInvUpdateService;

    @Autowired
    public void setOtherInsertInventory(IOInvUpdateService ioInvUpdateService) {
        this.ioInvUpdateService = ioInvUpdateService;
    }

    @Override
    public String execute(String param) {

        TenantUtils.execute(1L, () ->ioInvUpdateService.otherSellingInsertC5());

        return "已完成";
    }
}
