package cn.iocoder.yudao.module.therapy.dal.dataobject.report;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 数据统计 DO
 *
 * @author CBI系统管理员
 */
@TableName("hlgyy_stat")
@KeySequence("hlgyy_stat_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 统计类型
     */
    private Integer statType;
    /**
     * 统计数据
     */
    private BigDecimal statData;
    /**
     * 备注
     */
    private String remark;
    /**
     * 患者id
     */
    private Long userId;

}