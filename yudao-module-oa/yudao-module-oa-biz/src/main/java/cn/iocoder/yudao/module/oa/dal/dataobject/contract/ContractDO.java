package cn.iocoder.yudao.module.oa.dal.dataobject.contract;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 合同 DO
 *
 * @author 管理员
 */
@TableName("oa_contract")
@KeySequence("oa_contract_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 公司类型
     */
    private String companyType;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 供方代表
     */
    private Long supplierUserId;
    /**
     * 总款
     */
    private BigDecimal totalFee;
    /**
     * 劳务费
     */
    private BigDecimal serviceFee;
    /**
     * 佣金
     */
    private BigDecimal commissions;
    /**
     * 零星费用
     */
    private BigDecimal otherFee;
    /**
     * 工程实施联系人
     */
    private String implContactName;
    /**
     * 工程实施联系电话
     */
    private String implContactPhone;
    /**
     * 合同状态
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
