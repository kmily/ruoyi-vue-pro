package cn.iocoder.yudao.framework.tenant.core.mq.rocketmq;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;

import java.util.List;

import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * RocketMQ 消息队列的多租户 {@link MessageListenerOrderly} 实现类，支持批量消费消息的租户上下文设置
 * 默认实现 {@link DefaultRocketMQListenerContainer.DefaultMessageListenerOrderly}
 *
 * @author changhao.ni
 */
@Slf4j
public class TenantRocketMQListenerOrderly implements MessageListenerOrderly {

    private final DefaultRocketMQListenerContainer container;

    TenantRocketMQListenerOrderly(DefaultRocketMQListenerContainer container) {
        this.container = container;
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt messageExt : msgs) {
            log.debug("received msg: {}", messageExt);
            try {
                long now = System.currentTimeMillis();

                // 设置租户编号
                String tenantId = messageExt.getUserProperty(HEADER_TENANT_ID);
                if (StrUtil.isNotEmpty(tenantId)) {
                    TenantContextHolder.setTenantId(Long.parseLong(tenantId));
                }

                container.handleMessage(messageExt);

                long costTime = System.currentTimeMillis() - now;
                log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
            } catch (Exception e) {
                log.warn("consume message failed. messageId:{}, topic:{}, reconsumeTimes:{}", messageExt.getMsgId(), messageExt.getTopic(), messageExt.getReconsumeTimes(), e);
                context.setSuspendCurrentQueueTimeMillis(container.getSuspendCurrentQueueTimeMillis());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            } finally {
                // 清理租户编号
                TenantContextHolder.clear();
            }
        }

        return ConsumeOrderlyStatus.SUCCESS;
    }
}
