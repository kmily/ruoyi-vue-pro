package cn.iocoder.yudao.module.im.dal.dataobject.message.content;

import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import lombok.Data;

/**
 * 视频消息的 {@link MessageDO 字段 content} 的内容
 *
 * @author 芋道源码
 */
@Data
public class VideoMessage {

    /**
     * 视频地址
     */
    private String url;

}