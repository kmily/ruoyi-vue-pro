package cn.iocoder.yudao.module.im.enums.message;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * IM 消息的类型枚举
 * <p>
 * 参考 <a href="https://doc.yunxin.163.com/messaging/docs/zg3NzA3NTA?platform=web#消息类型">“消息类型”</a> 文档
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum ImMessageTypeEnum implements IntArrayValuable {

    TEXT(1, "文本"), // 消息内容为普通文本
    IMAGE(2, "图片"), // 消息内容为图片 URL 地址、尺寸、图片大小等信息
    AUDIO(3, "语音"), // 消息内容为语音文件的 URL 地址、时长、大小、格式等信息
    VIDEO(4, "视频"), // 消息内容为视频文件的 URL 地址、时长、大小、格式等信息
    FILE(5, "文件"), // 消息内容为文件的 URL 地址、大小、格式等信息
    LOCATION(6, "地理位置"), // 消息内容为地理位置标题、经度、纬度信息
    // TODO @芋艿：下面两种，貌似企业微信设计的更好：https://developer.work.weixin.qq.com/document/path/90240
    TIP(7, "提示"), // 又叫做 Tip 消息，没有推送和通知栏提醒，主要用于会话内的通知提醒，例如进入会话时出现的欢迎消息，或是会话过程中命中敏感词后的提示消息等场景
    NOTIFICATION(8, "通知"), // 主要用于群组、聊天室和超大群的事件通知，由服务端下发，客户端无法发送事件通知消息。通知类消息有在线、离线、漫游机制；没有通知栏提醒
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ImMessageTypeEnum::getType).toArray();
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
}