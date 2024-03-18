package cn.iocoder.yudao.module.steam.job.fin;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.steam.InvSellCashStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单自动检测发货
 */
@Slf4j
@Component
public class InvCheckTransferOrderJob implements JobHandler {
    @Resource
    private InvOrderMapper invOrderMapper;

    private PaySteamOrderService paySteamOrderService;
    @Autowired
    public void setPaySteamOrderService(PaySteamOrderService paySteamOrderService) {
        this.paySteamOrderService = paySteamOrderService;
    }
    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> {
            List<InvOrderDO> invOrderDOS = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                    .eq(InvOrderDO::getPayStatus, true)
                    .eqIfPresent(InvOrderDO::getTransferStatus, InvTransferStatusEnum.TransferFINISH.getStatus())
                    .eq(InvOrderDO::getSellCashStatus, InvSellCashStatusEnum.INIT.getStatus())
            );
            log.info("invorder{}",invOrderDOS);
            Integer integer=0;
            for (InvOrderDO invOrderDO:invOrderDOS) {
                paySteamOrderService.checkTransfer(invOrderDO.getId());
                integer++;
            }
            return integer;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
