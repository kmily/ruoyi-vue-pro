package cn.iocoder.yudao.module.promotion.mq.consumer.coupon;

import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.yudao.module.promotion.mq.message.coupon.UserCreateMessage;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 针对 {@link UserCreateMessage} 的消费者
 *
 * @author owen
 */
@Component
@Slf4j
public class UserCreateConsumer extends AbstractStreamMessageListener<UserCreateMessage> {

    @Resource
    private CouponService couponService;

    @Override
    public void onMessage(UserCreateMessage message) {
        log.info("[onMessage][消息内容({})]", message);
        couponService.takeCouponByRegister(message.getUserId());
    }

}
