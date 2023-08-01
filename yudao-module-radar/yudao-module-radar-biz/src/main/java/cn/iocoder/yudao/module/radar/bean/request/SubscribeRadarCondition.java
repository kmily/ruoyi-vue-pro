package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author whycode
 * @title: SubscribeRadarCondition
 * @projectName NETDemo(Java)
 * @description: TODO
 * @date 2023/4/2416:44
 */

@Data
@Getter
@Setter
public class SubscribeRadarCondition {

    /**
     * 按BIT位进行描述，每个BIT位，对应一种订阅类型，从左到右依次为第0位-第31位，相应的BIT为1代表订阅类型有效。
     * 车辆交通数据：
     * Bit2：区域&统计&排队流量
     * Bit3：即时&过车数量
     * Bit4：事件数据
     * 结构化数据：
     * Bit5：结构化过车数据
     * Bit6：结构化违法数据
     */
    @JSONField(name = "Type")
    private long type;

}
