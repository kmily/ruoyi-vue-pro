package cn.iocoder.yudao.module.im.dal.dataobject.message.content;

import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片消息的 {@link MessageDO 字段 content} 的内容
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMessage {

    /**
     * 图片地址
     */
    private String url;

}
