package cn.iocoder.yudao.module.radar.bean.entity;

import cn.iocoder.yudao.module.radar.enums.DeviceTypeEnum;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: RequestData
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/215:25
 */

@Data
@ToString
public class RequestData {

    /**
     * 数据类型
     */
    private DeviceTypeEnum type;

    /**
     * 上报时间。
     * UTC 时间，从 1970 年 1 月 1 日
     * 0 点开始的秒数。
     */
    @JSONField(name = "TimeStamp")
    private Long timeStamp;

    /**
     *推送数据序号。
     * IPC 使用。
     */
    @JSONField(name = "Seq")
    private Integer seq;

    /**
     * 设备编码或域编码，回传事件订
     * 阅下发的设备编码。
     * 当事件订阅接口中携带设备编码
     * 时必填。
     * 长度范围[0, 32]
     */
    @JSONField(name = "DeviceID")
    private String deviceCode;

    /**
     * 告警源类型，长度范围[0,32]
     * 详见附件通道类型
     */
    @JSONField(name = "SourceType")
    private String sourceType;

    /**
     * 区域规则数量，从 0 开始，0 代
     * 表无区域规则上报。
     */
    @JSONField(name = "AreaNum")
    private int areaNum;

    /**
     *绊线规则数量，从 0 开始。0 代
     * 表无绊线规则上报
     */
    @JSONField(name = "LineNum")
    private int lineNum;

    /**
     * 人员体征数据，上报周期相同，
     * 统计周期默认与上报周期一致，
     * 最小为 60 秒
     */
    @JSONField(name = "HealthData")
    private HealthData healthData;

    /**
     * 区域规则统计数据，AreaNum 非
     * 0 时必选。
     */
    @JSONField(name = "AreaRuleDataList")
    private List<AreaRuleData> areaRuleDataList;

    /**
     * 绊线规则统计的数据，LineNum
     * 非 0 时必选。
     */
    @JSONField(name = "LineRuleDataList")
    private List<LineRuleData>  lineRuleDataList;

}
