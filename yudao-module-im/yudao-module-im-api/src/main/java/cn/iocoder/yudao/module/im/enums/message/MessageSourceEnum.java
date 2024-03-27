package cn.iocoder.yudao.module.im.enums.message;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * IM 消息的消息来源 100-用户发送；200-系统发送（一般是通知）；不能为空
 */
@RequiredArgsConstructor
@Getter
public enum MessageSourceEnum implements IntArrayValuable {

    USER_SEND(100, "用户发送"),
    SYSTEM_SEND(200, "系统发送");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(MessageSourceEnum::getStatus).toArray();

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 名字
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}