package cn.iocoder.yudao.module.steam.job.selling;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Slf4j
@Component
public class OtherSellingPullJob implements JobHandler {

    private IOInvUpdateService ioInvUpdateService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setOtherInsertInventory(IOInvUpdateService ioInvUpdateService) {
        this.ioInvUpdateService = ioInvUpdateService;
    }

    @Override
    public String execute(String param) {
        Integer execute = TenantUtils.execute(1L, () -> {

            ioInvUpdateService.otherSellingInsert();
            rabbitTemplate.convertAndSend("steam", "steam_other_selling");
            return 1;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
