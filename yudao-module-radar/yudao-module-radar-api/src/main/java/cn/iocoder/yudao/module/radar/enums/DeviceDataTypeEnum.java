package cn.iocoder.yudao.module.radar.enums;

/**
 * @author whycode
 * @title: DeviceTypeEnum
 * @projectName ruoyi-vue-pro
 * @description: 设备类型
 * @date 2023/8/39:32
 */
public enum DeviceDataTypeEnum {

    HEALTH(1, "体征雷达"),
    AREA_RULE(2, "区域统计"),
    LINE_RULE(3, "绊线统计");
    public final int type;
    public final String name;
    DeviceDataTypeEnum(int type, String name){
        this.type = type;
        this.name = name;
    }

}
