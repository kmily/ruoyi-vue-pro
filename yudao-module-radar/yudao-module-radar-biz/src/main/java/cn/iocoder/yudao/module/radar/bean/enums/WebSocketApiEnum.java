package cn.iocoder.yudao.module.radar.bean.enums;

import org.apache.logging.log4j.Level;

/**
 * Created by l09655 on 2022/8/8.
 * 对接接口封装枚举类
 */
public enum WebSocketApiEnum {
    KEEPALIVE("/LAPI/V1.0/System/UpServer/Keepalive", "设备保活", LogEnum.CONSOLE),
    REAL_TIME_INFO("/LAPI/V1.0/System/Radar/Service/RealTimeInfo", "实时数据", LogEnum.CONSOLE),
    ALARM("/LAPI/V1.0/System/Event/Notification/Alarm", "实时推送告警事件", LogEnum.CONSOLE),
    REGISTER("/LAPI/V1.0/System/UpServer/Register", "设备注册", LogEnum.CONSOLE),
    SUBSCRIPTION("/LAPI/V1.0/System/Event/Subscription", "订阅告警", LogEnum.CONSOLE),
    OBJECT_REAL_TIME("/LAPI/V1.0/System/Event/Notification/ObjectRealTimeData", "实时数据接口", LogEnum.DATA_OBJECT_REAL_TIME),
    PASS_DATA("/LAPI/V1.0/System/Event/Notification/PassData", "过车数据接口", LogEnum.DATA_PASS),
    ROAD_FLOW("/LAPI/V1.0/System/Event/Notification/RoadFlow", "交通区域接口", LogEnum.DATA_ROAD_FLOW),
    TRAFFIC_FLOW("/LAPI/V1.0/System/Event/Notification/TrafficFlow", "交通统计接口", LogEnum.DATA_TRAFFIC_FLOW),
    VEHICLE_QUEUE_LEN("/LAPI/V1.0/System/Event/Notification/VehicleQueueLen", "车辆排队信息接口", LogEnum.DATA_VEHICLE_QUEUE_LEN),
    RADAR_DETECTION("/LAPI/V1.0/System/Event/Notification/RadarDetection", "雷达事件接口", LogEnum.DATA_RADAR_DETECTION),
    LINE_RULE_DATA("/LAPI/V1.0/System/Event/Notification/PeopleCount/LineRuleData", "人数拌线统计接口", LogEnum.DATA_LINE_RULE),
    AREA_RULE_DATA("/LAPI/V1.0/System/Event/Notification/PeopleCount/AreaRuleData", "人数区域统计接口", LogEnum.DATA_AREA_RULE),
    HEALTH_DATA("/LAPI/V1.0/System/Event/Notification/HealthData", "人体康养接口", LogEnum.DATA_HEALTH),
    STRUCTURE_DATA("/LAPI/V1.0/System/Event/Notification/Structure", "结构化数据接口", LogEnum.DATA_STRUCTURE);

    public final String url;

    public final String apiName;


    public final Level logLevel;

    WebSocketApiEnum(String url, String apiName, Level logLevel) {
        this.url = url;
        this.apiName = apiName;
        this.logLevel = logLevel;
    }

    /**
     * @param url
     * @return bean.enums.WebSocketApiEnum
     * @description: TODO 根据url获取接口信息
     * @author l09655 2022年08月08日
     */
    public static WebSocketApiEnum getApiByUrl(String url) {
        for(WebSocketApiEnum webSocketApiEnum : WebSocketApiEnum.values()) {
            if(url.equals(webSocketApiEnum.url)) {
                return webSocketApiEnum;
            }
        }
        return null;
    }

}
