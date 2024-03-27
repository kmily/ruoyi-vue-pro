package cn.iocoder.yudao.module.im.enums.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * IM 会话类型枚举
 * 参考 <a href="https://doc.rentsoft.cn/zh-Hans/sdks/enum/conversationType">“会话类型”</a> 文档
 *
 * @author anhaohao
 */
@Getter
@AllArgsConstructor
public enum ConversationTypeEnum {

    SINGLE(1, "单聊"),
    GROUP(3, "群聊"),
    NOTIFICATION(4, "通知会话");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 名字
     */
    private final String name;

    /**
     * 生成会话编号
     * @param fromUserId 发送者编号
     * @param receiverId 接收者编号
     * @param conversationType 会话类型
     * @return 会话编号
     */
    public static String generateConversationNo(Long fromUserId, Long receiverId, Integer conversationType) {
        if (conversationType.equals(ConversationTypeEnum.SINGLE.getType())) {
            return "s_" + fromUserId + "_" + receiverId;
        } else if (conversationType.equals(ConversationTypeEnum.GROUP.getType())) {
            return "g_" + receiverId;
        }
        return null;
    }

}