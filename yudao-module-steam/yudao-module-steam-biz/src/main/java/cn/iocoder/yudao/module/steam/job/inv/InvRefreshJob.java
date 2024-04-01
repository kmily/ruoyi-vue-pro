package cn.iocoder.yudao.module.steam.job.inv;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 库存订单超时自动关闭相关数据表
 * 此接口是因为支付关闭时未作回调，所以做计划主动查询
 */
@Slf4j
@Component(value = "InvRefreshJob")
public class InvRefreshJob implements JobHandler {
    @Resource
    private BindUserMapper bindUserMapper;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public String execute(String param) {
        Integer execute = TenantUtils.execute(1L, () -> {
            List<BindUserDO> bindUserDOS = bindUserMapper.selectList( new LambdaQueryWrapperX<BindUserDO>()
                    .eq(BindUserDO::getIsTempAccount, false)
                    .isNotNull(BindUserDO::getSteamPassword)
            );
            bindUserDOS.forEach(
                    item->{
                        rabbitTemplate.convertAndSend("steam","steam_inv",item.getId());
                    }
            );
            return bindUserDOS.size();
        });
        return String.format("执行符合要求的数据量 %s 个", execute);
    }
}
