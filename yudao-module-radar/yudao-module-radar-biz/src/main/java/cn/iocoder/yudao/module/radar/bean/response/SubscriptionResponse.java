package cn.iocoder.yudao.module.radar.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by l09655 on 2022/8/2.
 * 数据订阅接口返回响应实体类
 */
@Data
@Getter
@Setter
public class SubscriptionResponse {

    /**
     * 跟参数Reference中的URL最后一段相同，用以识别哪次订阅，刷新订阅、删除订阅操作需要携带此ID，以指明需操作哪次订阅。
     */
    @JSONField(name = "ID")
    private long id;

    /**
     * 订阅者描述信息，以URL格式体现。
     * 短连接必选。
     */
    @JSONField(name = "Reference")
    private String reference;

    @JSONField(name = "CurrentTime")
    private long currentTime;

    @JSONField(name = "TerminationTime")
    private long terminationTime;

    /**
     * 支持类型。
     * 按BIT位进行描述，每个BIT位，对应一种订阅类型，从右到左依次为第0位-第31位，相应的BIT为1代表订阅类型有效。
     * Bit5：结构化数据
     * Bit11：车辆交通数据
     * Bit14：人数统计
     * Bit17：人体康养
     * 订阅返回时返回设备具体支持的告警类型有哪些。
     */
    @JSONField(name = "SupportType")
    private long supportType;

}
