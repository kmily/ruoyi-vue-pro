package cn.iocoder.yudao.module.steam.mq.io661;

import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.inv.InvExtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
@Slf4j
@RabbitListener(queues = "steam_inv",concurrency = "1")
public class IvnConsumer {
    @Resource
    private InvExtService invExtService;
    @Resource
    private BindUserService bindUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RabbitHandler // 重点：添加 @RabbitHandler 注解，实现消息的消费
    public void onMessage(Long message) {
        log.info("[onMessage][消息内容({})]", message);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BindUserDO bindUser = bindUserService.getBindUser(message);
        if(Objects.isNull(bindUser)){
            return;
        }
        if(bindUser.getIsTempAccount()){
            return;
        }
        if(Objects.isNull(bindUser.getSteamPassword())){
            return;
        }
        if(Objects.isNull(bindUser.getSteamId())){
            return;
        }
        invExtService.fetchInv(bindUser);
        stringRedisTemplate.delete("IVN_Fetch"+message);
    }
}
