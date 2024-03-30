package cn.iocoder.yudao.module.im.enums.message;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

// TODO @anhaohao：IM 前缀还是要的哈
// TODO TODO 状态是这些哈，客户端的视角：
//
//- 草稿（预留） 0
//- 发送中 1
//- 发送成功 2
//- 发送失败 3
//- 已删除 4
//- 已撤回 5
/**
 * IM 消息的状态枚举
 */
@RequiredArgsConstructor
@Getter
public enum MessageStatusEnum implements IntArrayValuable {

    SENDING(1, "发送中"),
    SUCCEED(2, "发送成功"),
    FAILED(3, "发送失败");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(MessageStatusEnum::getStatus).toArray();

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