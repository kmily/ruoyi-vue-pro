package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by l09655 on 2022/7/27.
 * 数据订阅接口调用请求实体类
 */
@Data
@Getter
@Setter
public class SubscriptionRequest {

    /**
     * 数据发送目的IP地址类型：
     * 0: IPv4；
     * 1: IPv6；
     * 2:域名；
     * 3: IPv4和IPv6都需要；
     * 当前仅支持IPv4。
     */
    @JSONField(name = "AddressType")
    private long addressType = 0;

    /**
     * 数据发送目的IPv4地址。
     * 长度范围为[0,64]。
     */
    @JSONField(name = "IPAddress")
    private String ipAddress;

    /**
     * 数据发送目的端口，范围为[1, 65535]。
     */
    @JSONField(name = "Port")
    private long port;

    /**
     * 设备编码或域编码，回传事件订阅下发的设备编码。
     * 当事件订阅接口中携带设备编码时必填。
     * 长度范围[0, 32]
     */
    @JSONField(name = "DeviceID")
    private String deviceID;

    /**
     * 订阅周期，单位为s，范围为[1, 3600]
     */
    @JSONField(name = "Duration")
    private long duration = 3600L;

    /**
     * 按BIT位进行描述，每个BIT位，对应一种订阅类型，从低位到高位依次为第0位-第31位，相应的BIT为1代表订阅类型有效。
     * Bit5：结构化数据
     * Bit11：车辆交通数据
     * Bit14：人数统计
     * Bit17：人体康养
     * 例：Bit14置位对应十进制数为16384
     */
    @JSONField(name = "Type")
    private long type;

    /**
     * 雷达事件订阅图片类型，仅订阅雷达事件时传，所以需要可以为null，用Long类型
     */
    @JSONField(name = "ImagePushMode")
    private Long imagePushMode;

    /**
     * 当订阅类型中包含车流量数据或车牌识别时携带
     */
    @JSONField(name = "SubscribeVehicleCondition")
    private SubscribeVehicleCondition subscribeVehicleCondition;

    @JSONField(name = "SubscribeRadarCondition")
    private SubscribeRadarCondition subscribeRadarCondition;

    @JSONField(name = "SubscribeEvent")
    private SubscribeEvent subscribeEvent;

}
