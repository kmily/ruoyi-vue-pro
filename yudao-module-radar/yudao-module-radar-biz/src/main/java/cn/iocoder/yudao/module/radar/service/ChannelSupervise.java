package cn.iocoder.yudao.module.radar.service;

import cn.iocoder.yudao.module.radar.bean.entity.Device;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelSupervise {
    public static ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ConcurrentMap<ChannelId, ChannelHandlerContext> ChannelMap = new ConcurrentHashMap<>();

    /**
     * ChannelId和成功注册的设备信息map
     * 建立websocket连接前存储，建立失败通道关闭时删除
     */
    public static ConcurrentHashMap<ChannelId, Device> channelDeviceMap = new ConcurrentHashMap<>();

    /**
     * ChannelId和当前通道最近一次发送数据订阅请求的时间戳map
     * 建立websocket连接前设置为0，建立失败通道关闭时删除，发送订阅请求或刷新订阅请求时修改为当前时间戳
     */
    public static ConcurrentHashMap<ChannelId, Long> channelSubTimeMap = new ConcurrentHashMap<>();

    /**
     * 设备编号，是否出于连接状态
     *  设备编号
     */
    public static ConcurrentMap<String, Boolean> keepaliveMap = new ConcurrentHashMap<>();


    /**
     * 发送请求自增常量，协议需要，作用不明，后续可能需要持久化用自增序列实现
     */
    public static long Cseq = 1;

    public static void addChannel(ChannelHandlerContext ctx) {
        GlobalGroup.add(ctx.channel());
        ChannelMap.put(ctx.channel().id(), ctx);
    }

    /*public static List<Channel> getAllChannel() {
        List<Channel> list = new ArrayList<>();
        for (Map.Entry<String, ChannelId> entry : ChannelMap.entrySet()){
            Channel channel = GlobalGroup.find(entry.getValue());
            list.add(channel);
        }
        return list;
    }*/

    public static void removeChannel(Channel channel) {
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id());
        channelDeviceMap.remove(channel.id());
        channelSubTimeMap.remove(channel.id());
    }

    /*public static Channel findChannel(String id) {
        return GlobalGroup.find(ChannelMap.get(id));
    }*/

    public static void send2All(TextWebSocketFrame tws) {
        GlobalGroup.writeAndFlush(tws);
    }
}
