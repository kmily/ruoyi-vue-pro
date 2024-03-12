package cn.iocoder.yudao.module.im.dal.dataobject.message.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 语音消息的 {@link ImMessageBody}
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImAudioMessageBody implements ImMessageBody {

    /**
     * 语音 URL
     */
    private String url;
    /**
     * 语音格式，例如说 arm、mp3、speex 等
     */
    private String format;

    // TODO 芋艿：要不要以下字段？待定；云信有、企业微信没有
//"dur":4551,		//语音持续时长ms
//        "md5":"87b94a090dec5c58f242b7132a530a01",	//语音文件的md5值，按照字节流加密
//        "size":16420		//语音文件大小，单位为字节（Byte）

}