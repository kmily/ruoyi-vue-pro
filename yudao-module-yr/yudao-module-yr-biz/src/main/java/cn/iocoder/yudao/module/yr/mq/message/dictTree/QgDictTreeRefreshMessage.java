package cn.iocoder.yudao.module.yr.mq.message.dictTree;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务字典分类数据刷新 Message
 *
 * @author alex
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QgDictTreeRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "qg.dictTree.refresh";
    }

}
