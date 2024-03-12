package cn.iocoder.yudao.module.im.dal.dataobject.message.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片消息的 {@link ImMessageBody}
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImImageMessageBody implements ImMessageBody {

    /**
     * 图片地址
     */
    private String url;

    // TODO 芋艿：要不要以下字段？待定；云信有、企业微信没有
//    "name":"图片发送于2015-05-07 13:59",   //图片name
//            "md5":"9894907e4ad9de4678091277509361f7",	//图片文件md5，按照字节流加密
//            "ext":"jpg",	//图片后缀
//            "w":6814,	//宽，单位为像素
//            "h":2332,	//高，单位为像素
//            "size":388245	//图片文件大小，单位为字节（Byte）

}
