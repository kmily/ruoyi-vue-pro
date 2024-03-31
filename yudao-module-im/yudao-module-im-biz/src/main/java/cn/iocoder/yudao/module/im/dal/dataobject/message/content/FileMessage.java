package cn.iocoder.yudao.module.im.dal.dataobject.message.content;

import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件消息的 {@link ImMessageDO 字段 content} 的内容
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMessage {

    /**
     * 文件名
     */
    private String name;
    /**
     * 文件 URL
     */
    private String url;

}