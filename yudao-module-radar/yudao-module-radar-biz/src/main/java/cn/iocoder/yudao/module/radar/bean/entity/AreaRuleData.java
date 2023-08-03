package cn.iocoder.yudao.module.radar.bean.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author whycode
 * @title: AreaRuleData
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/38:43
 */

@Data
public class AreaRuleData {

    /**
     * 检测区域 ID 号，从 0 开始。
     */
    @JSONField(name = "AreaID")
    private long areaID;

    /**
     * 区域规则当前人数
     */
    @JSONField(name = "ObjectNum")
    private long objectNum;

}
