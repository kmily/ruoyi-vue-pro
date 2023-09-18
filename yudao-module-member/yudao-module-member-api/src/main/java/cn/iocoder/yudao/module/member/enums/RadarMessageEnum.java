package cn.iocoder.yudao.module.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whycode
 * @title: RadarMessageEnum
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1510:41
 */
@Getter
@AllArgsConstructor
public enum RadarMessageEnum {

    FALL_ALARM_NOTICE("fall_alarm_notice", "跌倒检测雷达检测到有人{title}！时间：{time} 地点：{address}"), // 跌倒警告
    DETECTION_ALARM_NOTICE("detection_alarm_notice", "人体检测雷达检测到有人{title}！ 时间：{time} 地点：{address}"), //人体检测雷达
    EXIST_ALARM_NOTICE("exist_alarm_notice", "人员存在感知雷达检测到有人{title}！ 时间：{time} 地点：{address}"), //存在感知雷达
    HEALTH_ALARM_NOTICE("health_alarm_notice", "体征监测雷达检测到{} ！ 时间：{} 地点：{}");// 体征雷达 ; // 告警通知

    private final String code;

    private final String template;

}
