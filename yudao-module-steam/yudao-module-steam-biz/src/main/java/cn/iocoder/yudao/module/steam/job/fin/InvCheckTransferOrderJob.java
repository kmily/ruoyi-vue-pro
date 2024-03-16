package cn.iocoder.yudao.module.steam.job.fin;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存订单超时自动关闭相关数据表
 * 此接口是因为支付关闭时未作回调，所以做计划主动查询
 */
@Slf4j
@Component
public class InvCheckTransferOrderJob implements JobHandler {
    private SteamService steamService;
    @Autowired
    public void setSteamService(SteamService steamService) {
        this.steamService = steamService;
    }

    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> steamService.checkTransfer());
        return String.format("执行关闭成功 %s 个", execute);
    }
}
