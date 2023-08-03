package cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 区域数据 DO
 *
 * @author 芋道源码
 */
@TableName("radar_area_rule_data")
@KeySequence("radar_area_rule_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaRuleDataDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 上报时间
     */
    private Long timeStamp;
    /**
     * 消息序号。用于判定数据连续性
     */
    private Integer seq;
    /**
     * 绊线规则数量
     */
    private Integer areaNum;
    /**
     * 区域统计数据
     */
    private String areaData;

}
