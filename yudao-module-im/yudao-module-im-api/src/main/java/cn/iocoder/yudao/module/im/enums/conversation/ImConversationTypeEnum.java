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

    // TODO @hao：单聊，我们使用 SINGLE，主要 private 这个单词在 java 里太特殊了；
    PRIVATE(1, "单聊"),
    GROUP(2, "群聊"),
    NOTICE(4, "通知会话");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;

}