package cn.iocoder.yudao.module.im.dal.dataobject.message.content;

import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO @anhaohao：要有 IM；
// TODO @芋艿：后续要挪到 api 包下，主要是给外部接口使用
/**
 * 语音消息的 {@link MessageDO 字段 content} 的内容
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioMessage {

    /**
     * 语音文件唯一 ID
     */
    private String uuid;

    /**
     * 语音文件的本地路径
     */
    private String soundPath;

    /**
     * 语音文件下载地址
     */
    private String sourceUrl;

    /**
     * 语音文件大小
     */
    private int dataSize;

    /**
     * 语音时长
     */
    private int duration;

    /**
     * 语音文件类型
     */
    private String soundType;

}