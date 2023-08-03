package cn.iocoder.yudao.module.radar.cache;

import cn.iocoder.yudao.module.radar.bean.entity.RequestData;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author whycode
 * @title: RadarDataCache
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/215:41
 */
public class RadarDataCache {

    private static final BlockingQueue<RequestData> QUEUE = new LinkedBlockingQueue<>();



    public static RequestData take() throws InterruptedException {
        return QUEUE.take();
    }

    public static void put(RequestData requestData) throws InterruptedException {
        QUEUE.put(requestData);
    }

}
