package cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 报销明细 DO
 *
 * @author 管理员
 */
@TableName("oa_expenses_detail")
@KeySequence("oa_expenses_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesDetailDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 明细类型
     *
     * 枚举 {@link TODO oa_expense_travel_type 对应的类}
     */
    private String detailType;
    /**
     * 消费时间
     */
    private LocalDateTime consumeTime;
    /**
     * 报销费用
     */
    private BigDecimal detailFee;
    /**
     * 明细备注
     */
    private String detailRemark;
    /**
     * 报销申请id
     */
    private Long expensesId;

}
