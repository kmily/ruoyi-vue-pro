package cn.iocoder.yudao.module.radar.service;


import cn.iocoder.yudao.module.radar.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketChannelInit extends ChannelInitializer<SocketChannel> {


    private String currIp;
    private int currPort;
    private int registerType;

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("http-codec", new HttpServerCodec());//设置解码器
        //ch.pipeline().addLast("frameEncoder", new WebSocket13FrameEncoder(true));
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        ch.pipeline().addLast("handler", new WebSocketHandler(registerType));
    }

    public WebSocketChannelInit( String currIp, int currPort, int registerType) {
        this.currIp = currIp;
        this.currPort = currPort;
        this.registerType = registerType;
    }

}