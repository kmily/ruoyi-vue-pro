package cn.iocoder.yudao.module.radar.bean.enums;

import org.apache.logging.log4j.Level;


/**
 * Created by l09655 on 2022/8/1.
 * 日志静态变量定义
 */
public class LogEnum {

    public static final Level CONSOLE = Level.forName("console_log", 90);
    public static final Level DATA_OBJECT_REAL_TIME = Level.forName("object_real_time_log", 190);
    public static final Level DATA_PASS = Level.forName("pass_log", 180);
    public static final Level DATA_ROAD_FLOW = Level.forName("road_flow_log", 170);
    public static final Level DATA_TRAFFIC_FLOW = Level.forName("traffic_flow_log", 160);
    public static final Level DATA_VEHICLE_QUEUE_LEN = Level.forName("vehicle_queue_len_log", 150);
    public static final Level DATA_RADAR_DETECTION = Level.forName("radar_detection_log", 140);
    public static final Level DATA_LINE_RULE = Level.forName("line_rule_log", 130);
    public static final Level DATA_AREA_RULE = Level.forName("area_rule_log", 120);
    public static final Level DATA_HEALTH = Level.forName("health_log", 110);
    public static final Level DATA_STRUCTURE = Level.forName("structure_log", 100);

}
