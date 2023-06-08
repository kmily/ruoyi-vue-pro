package cn.iocoder.yudao.module.oa.dal.dataobject.expenses;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.xml.bind.v2.TODO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销申请 DO
 *
 * @author 管理员
 */
@TableName("oa_expenses")
@KeySequence("oa_expenses_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesDO extends BaseDO {

    /**
     * 报销id
     */
    @TableId
    private Long id;
    /**
     * 报销类型
     *
     * 枚举 {@link TODO oa_expense_type 对应的类}
     */
    private String expensesType;
    /**
     * 展会名称
     */
    private String exhibitName;
    /**
     * 展会开始时间
     */
    private LocalDateTime exhibitBeginDate;
    /**
     * 展会结束时间
     */
    private LocalDateTime exhibitEndDate;
    /**
     * 展会地点
     */
    private String exhibitAddress;
    /**
     * 关联的拜访过的客户
     */
    private Long customerId;
    /**
     * 费用说明
     */
    private String feeRemark;
    /**
     * 报销总费用
     */
    private BigDecimal fee;
    /**
     * 申请单状态
     */
    private int status;
    /**
     * 审批状态
     */
    private int approvalStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
