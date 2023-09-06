package cn.iocoder.yudao.module.radar.server;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.radar.api.device.DeviceApi;
import cn.iocoder.yudao.module.radar.config.RadarProperties;
import cn.iocoder.yudao.module.radar.job.DeviceCache;
import cn.iocoder.yudao.module.radar.service.ApiSubThread;
import cn.iocoder.yudao.module.radar.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import sun.net.util.IPAddressUtil;

import javax.annotation.Resource;
import java.net.Inet4Address;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author whycode
 * @title: RadarSocketServer
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/213:43
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RadarProperties.class)
public class RadarSocketServer implements InitializingBean {

    /**
     * 设备注册模式
     */
    private static final int REGISTER_TYPE = 1;

    @Resource
    private RadarProperties properties;

    @Resource
    private DeviceCache deviceCache;


    @Override
    public void afterPropertiesSet() throws Exception {

        String host = properties.getHost();

        /*if(StrUtil.isEmpty(host)){
            host = Inet4Address.getLocalHost().getHostAddress();
        }*/

        log.info("[SOCKET] - 启动[socket] 地址和端口为: {}:{}", host, properties.getPort());

        WebSocketServer webSocketServer = new WebSocketServer(properties.getPort(), null, REGISTER_TYPE);

        /**
         * 启动Netty服务端，设置开启订阅按钮为enable
         */
        ExecutorService service1 = Executors.newSingleThreadExecutor();
        service1.submit(webSocketServer);
        service1.shutdown();

        int currPort = properties.getPort();
        String currIp = host;

        ApiSubThread.apiSubStatus = 1;
//        ApiSubThread.subType = getSubTypeFirst();
//        ApiSubThread.subTypeFlowRate = getSubTypeFlowRate();

        ApiSubThread apiSubThread = new ApiSubThread(currPort, currIp, deviceCache);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(apiSubThread);
        service.shutdown();

    }

}
