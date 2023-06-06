package com.cw.module.trade.dal.dataobject.position;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 账户持仓信息 DO
 *
 * @author chengjiale
 */
@TableName("account_position")
@KeySequence("account_position_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 持仓数量
     */
    private BigDecimal quantity;
    
    /** 持仓成本价 */
    private BigDecimal entryPrice;
    /**
     * 第三方数据
     */
    private String thirdData;
    
    /** 是否拥有持仓 */
    public boolean hasPosition() {
        return this.quantity != null && this.quantity.compareTo(new BigDecimal(0)) != 0;
    }
    
    /** 是否拥有做多持仓 */
    public boolean hasOverZeroPosition() {
        return this.quantity != null && this.quantity.compareTo(new BigDecimal(0)) == 1;
    }
    
    /** 是否拥有做空持仓 */
    public boolean hasSubZeroPosition() {
        return this.quantity != null && this.quantity.compareTo(new BigDecimal(0)) == -1;
    }

}
