package cn.iocoder.yudao.module.im.dal.dataobject.message.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件消息的 {@link ImMessageBody}
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImFileMessageBody implements ImMessageBody {

    /**
     * 文件名
     */
    private String name;
    /**
     * 文件 URL
     */
    private String url;

    // TODO 芋艿：要不要以下字段？待定；云信有、企业微信没有
//  "md5":"79d62a35fa3d34c367b20c66afc2a500", //文件MD5，按照字节流加密
//  "ext":"ttf",	//文件后缀类型
//  "size":91680	//大小，单位为字节（Byte）

}