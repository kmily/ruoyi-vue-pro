package cn.iocoder.yudao.module.im.enums.conversation;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// TODO @anhaohao：IM 前缀还是要的哈
/**
 * IM 会话类型枚举
 * 参考 <a href="https://doc.rentsoft.cn/zh-Hans/sdks/enum/conversationType">“会话类型”</a> 文档
 *
 * @author anhaohao
 */
@Getter
@AllArgsConstructor
public enum ConversationTypeEnum implements IntArrayValuable {

    SINGLE(1, "单聊"),
    GROUP(3, "群聊"),
    NOTIFICATION(4, "通知会话");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ConversationTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 名字
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    /**
     * 生成会话编号 TODO @anhaohao：方法注释，和下面参数之间，要有空格
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