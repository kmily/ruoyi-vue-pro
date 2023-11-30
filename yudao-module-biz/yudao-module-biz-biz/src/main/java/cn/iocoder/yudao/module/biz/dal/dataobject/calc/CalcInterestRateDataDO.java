package cn.iocoder.yudao.module.biz.dal.dataobject.calc;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 利率数据 DO
 *
 * @author 芋道源码
 */
@TableName("biz_calc_interest_rate_data")
@KeySequence("biz_calc_interest_rate_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalcInterestRateDataDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 开始日期
     */
    private Date startDate;
    private Date endDate;
    /**
     * 半年期利率
     */
    private BigDecimal rateHalfYear;
    /**
     * 一年期利率
     */
    private BigDecimal rateOneYear;
    /**
     * 三年期利率
     */
    private BigDecimal rateThreeYear;
    /**
     * 五年期利率
     */
    private BigDecimal rateFiveYear;
    /**
     * 五年以上利率
     */
    private BigDecimal rateOverFiveYear;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String creator;

    private String updater;

    private Boolean deleted;

    private Integer tenantId;


}
