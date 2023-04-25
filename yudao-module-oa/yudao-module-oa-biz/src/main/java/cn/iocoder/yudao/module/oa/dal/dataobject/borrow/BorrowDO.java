package cn.iocoder.yudao.module.oa.dal.dataobject.borrow;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

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
    private Boolean status;
    /**
     * 审批状态
     */
    private Boolean approvalStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;

}
