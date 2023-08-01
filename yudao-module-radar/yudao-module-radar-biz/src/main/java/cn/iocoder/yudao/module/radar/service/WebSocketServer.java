package cn.iocoder.yudao.module.radar.service;

import cn.iocoder.yudao.module.radar.utils.WebSocketUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Slf4j
public class WebSocketServer implements Runnable {

    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;

    private final int currPort;

    private final String currIp;

    private final int registerType;

    public WebSocketServer( int currPort, String currIp, int registerType) {

        this.currPort = currPort;
        this.currIp = currIp;
        this.registerType = registerType;
    }

    public void init() {
        log.info("正在启动websocket服务器...\n");

        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class).option(ChannelOption.SO_RCVBUF,  65536)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65536));

            bootstrap.childHandler(new WebSocketChannelInit( currIp, currPort, registerType));
            log.info("端口设置为" + currPort + "\n");
            Channel channel = null;
            try {
                channel = bootstrap.bind(currIp, currPort).sync().channel();
                log.info("webSocket服务器启动成功：" + channel + "\n");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("webSocket服务器启动失败，端口被占用或该端口已有服务运行，请检查ip端口设置\n");
            }
            assert channel != null;
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("websocket服务器已关闭\n");
        }
    }

    public void close() {
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    @Override
    public void run() {
        init();
    }

    public void closeSessions() {
        Set<ChannelId> channelIds = ChannelSupervise.ChannelMap.keySet();
        if (!CollectionUtils.isEmpty(channelIds)) {
            channelIds.forEach(WebSocketUtil::closeSession);
        }
    }
}
