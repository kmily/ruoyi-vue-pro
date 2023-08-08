package cn.iocoder.yudao.module.radar.enums;

/**
 * @author whycode
 * @title: RadarType
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/415:18
 */
public enum RadarType {
    HEALTH_AREA(1, "体征雷达"),
    EXIST_AREA(2, "存在感知雷达"),
    FAll_AREA(3, "跌倒雷达"),
    DETECTION_AREA(4, "人体检测雷达");
    public final int type;
    public final String name;

    RadarType(int type, String name){
        this.type = type;
        this.name = name;
    }

}
