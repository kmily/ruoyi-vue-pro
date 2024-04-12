package cn.iocoder.yudao.module.steam.job.fin;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.service.fin.ApiOrderService;
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
public class CheckTransferOrderJob implements JobHandler {
    @Resource
    private ApiOrderMapper apiOrderMapper;

    private ApiOrderService apiOrderService;
    @Autowired
    public void setApiOrderService(ApiOrderService apiOrderService) {
        this.apiOrderService = apiOrderService;
    }

    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> {
            List<ApiOrderDO> apiOrderDOS = apiOrderMapper.selectList(new LambdaQueryWrapperX<ApiOrderDO>()
                    .eq(ApiOrderDO::getPayOrderStatus, PayOrderStatusEnum.SUCCESS.getStatus())
                    .eqIfPresent(ApiOrderDO::getTransferStatus, InvTransferStatusEnum.TransferING.getStatus())
                    .eq(ApiOrderDO::getCashStatus, InvSellCashStatusEnum.INIT.getStatus())
            );
            log.info("apiOrderDOS{}",apiOrderDOS);
            Integer integer=0;
            for (ApiOrderDO item:apiOrderDOS) {
                apiOrderService.checkTransfer(item.getId());
                integer++;
            }
            return integer;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
