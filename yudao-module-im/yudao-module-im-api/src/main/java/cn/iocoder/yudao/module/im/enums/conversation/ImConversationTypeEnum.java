package cn.iocoder.yudao.module.im.enums.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * IM 会话的类型枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ImConversationTypeEnum {

    PRIVATE(1, "单聊"),
    GROUP(2, "群聊");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;

}