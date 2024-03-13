package cn.iocoder.yudao.module.im.enums.message;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ImMessageStatusEnum implements IntArrayValuable {

    SENDING(1, "发送中"),
    SUCCESS(2, "发送成功"),
    FAILURE(3, "发送失败"),
    DELETED(4, "已删除"),
    RECALL(5, "已撤回");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ImMessageStatusEnum::getStatus).toArray();
    private final Integer status;
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}