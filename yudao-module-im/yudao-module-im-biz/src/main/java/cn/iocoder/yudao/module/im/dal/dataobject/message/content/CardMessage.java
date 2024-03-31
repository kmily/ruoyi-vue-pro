package cn.iocoder.yudao.module.im.dal.dataobject.message.content;

import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 名片消息的 {@link ImMessageDO 字段 content} 的内容
 *
 * @author anhaohao
 * @since 2024/3/23 下午5:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardMessage {

    /**
     * 用户 ID
     */
    private String userID;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 用户头像
     */
    private int faceURL;

    /**
     * 扩展字段
     */
    private String ex;
}
