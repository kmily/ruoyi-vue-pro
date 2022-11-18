package cn.iocoder.yudao.module.yr.mq.message.dict;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务字典数据刷新 Message
 *
 * @author alex
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QgDictDataRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "qg.system.dict.data.refresh";
    }

}
