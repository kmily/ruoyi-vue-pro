package cn.iocoder.yudao.module.oa.dal.dataobject.borrow;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 借支申请 DO
 *
 * @author 管理员
 */
@TableName("oa_borrow")
@KeySequence("oa_borrow_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 说明
     */
    private String borrowReason;
    /**
     * 借支总费用
     */
    private BigDecimal borrowFee;
    /**
     * 已还款费用
     */
    private BigDecimal repaymentFee;
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
