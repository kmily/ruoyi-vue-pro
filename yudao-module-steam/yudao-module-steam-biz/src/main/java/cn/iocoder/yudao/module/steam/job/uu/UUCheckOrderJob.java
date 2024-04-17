package cn.iocoder.yudao.module.steam.job.uu;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
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
@Deprecated
//@Component("UUCheckOrderJob")
public class UUCheckOrderJob implements JobHandler {
    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    private UUOrderService uuOrderService;
    @Autowired
    public void setUuOrderService(UUOrderService uuOrderService) {
        this.uuOrderService = uuOrderService;
    }



    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> {
            List<YouyouOrderDO> uuOrders = youyouOrderMapper.selectList(new LambdaQueryWrapperX<YouyouOrderDO>()
                    .eq(YouyouOrderDO::getPayStatus, true)
                    .eqIfPresent(YouyouOrderDO::getTransferStatus, InvTransferStatusEnum.TransferING.getStatus())
            );
            log.info("order{}",uuOrders);
            int integer=0;
            for (YouyouOrderDO invOrderDO:uuOrders) {
                uuOrderService.checkTransfer(invOrderDO.getId());
                integer++;
            }
            return integer;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}
