package cn.iocoder.yudao.module.radar.bean.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author whycode
 * @title: LineRuleData
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/38:46
 */
@Data
public class LineRuleData {

    /**
     * 绊线 ID 号，从 0 开始。
     */
    @JSONField(name = "LineID")
    private long lineID;
    /**
     * 检测开始时间：
     * YYYYMMDDHHMMSS，时间按
     * 24 小时制。字符串长度[0,18]。
     */
    @JSONField(name = "BeginPassTime")
    private String beginPassTime;
    /**
     * 检测结束时间：
     * YYYYMMDDHHMMSS，时间按
     * 24 小时制。
     */
    @JSONField(name = "EndPassTime")
    private String endPassTime;
    /**
     *  配置时间内进入人数。
     */
    @JSONField(name = "ObjectIn")
    private long objectIn;
    /**
     * 配置时间内离开人数。
     */
    @JSONField(name = "ObjectOut")
    private long objectOut;
    /**
     * 总人数统计开始时间,：
     * YYYYMMDDHHMMSS，时间按
     * 24 小时制。字符串长度[0,18]。
     * 计数清零时重新记录开始时间
     */
    @JSONField(name = "TotalBeginTime")
    private String totalBeginTime;
    /**
     * 进入总人数，计数清零时恢复
     */
    @JSONField(name = "TotalIn")
    private long totalIn;
    /**
     * 离开总人数，计数清零时恢复
     */
    @JSONField(name = "TotalOut")
    private long totalOut;


}
