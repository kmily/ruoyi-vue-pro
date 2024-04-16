
package cn.iocoder.yudao.module.steam.mq.io661;

import cn.iocoder.yudao.module.steam.service.fin.ApiOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@RabbitListener(queues = "steam_inv_order_notify",concurrency = "1")
public class IvnOrderNotifyConsumer {
    @Resource
    private ApiOrderService apiOrderService;

    @RabbitHandler
    public void onMessage(Long message) {
        log.info("[onMessage][消息内容({})]", message);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        apiOrderService.pushRemote(Long.valueOf(message));
    }
}
