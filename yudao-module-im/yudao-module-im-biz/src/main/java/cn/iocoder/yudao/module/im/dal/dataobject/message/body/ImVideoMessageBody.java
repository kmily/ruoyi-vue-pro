package cn.iocoder.yudao.module.im.dal.dataobject.message.body;

import lombok.Data;

/**
 * 视频消息的 {@link ImMessageBody}
 *
 * @author 芋道源码
 */
@Data
public class ImVideoMessageBody implements ImMessageBody {

    /**
     * 视频地址
     */
    private String url;

    // TODO 芋艿：要不要以下字段？待定；云信有、企业微信没有
//  "dur":8003,		//视频持续时长ms
//          "md5":"da2cef3e5663ee9c3547ef5d127f7e3e",	//视频文件的md5值，按照字节流加密
//          "w":360,	//宽，单位为像素
//          "h":480,	//高，单位为像素
//          "size":16420	//视频文件大小，单位为字节（Byte）】
//      "ext":"mp4",	//视频格式


}