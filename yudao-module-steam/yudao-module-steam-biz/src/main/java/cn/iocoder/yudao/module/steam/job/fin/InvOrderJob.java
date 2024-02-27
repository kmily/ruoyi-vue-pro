package cn.iocoder.yudao.module.steam.job.fin;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 库存订单超时自动关闭相关数据表
 * 此接口是因为支付关闭时未作回调，所以做计划主动查询
 */
@Slf4j
@Component
public class InvOrderJob implements JobHandler {
    @Autowired
    private SteamService steamService;
    @Override
//    @TenantJob
    public String execute(String param) throws Exception {

        Integer execute = TenantUtils.execute(1l, () -> {
            return steamService.autoCloseInvOrder();
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
