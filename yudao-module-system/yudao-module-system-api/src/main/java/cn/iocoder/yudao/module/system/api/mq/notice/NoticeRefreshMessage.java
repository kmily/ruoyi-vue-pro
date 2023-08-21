package cn.iocoder.yudao.module.system.api.mq.notice;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author whycode
 * @title: NoticeRefreshMessage
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/2110:51
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeRefreshMessage extends AbstractChannelMessage {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息状态 0-开启，1-删除
     */
    private Byte status;

    @Override
    public String getChannel() {
        return "system.notice.refresh";
    }
}
