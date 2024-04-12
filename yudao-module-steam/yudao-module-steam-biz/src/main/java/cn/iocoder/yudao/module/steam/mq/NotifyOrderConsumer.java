package cn.iocoder.yudao.module.steam.mq;

import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Component
@Slf4j
//@RabbitListener(queues = "steam_order")
public class NotifyOrderConsumer {
    @Resource
    private UUOrderService uuOrderService;
    @RabbitHandler // 重点：添加 @RabbitHandler 注解，实现消息的消费
    public void onMessage(NotifyReq notifyReq) {
        log.info("[onMessage][消息内容({})]", notifyReq);
        uuOrderService.processNotify(notifyReq);
    }
}
